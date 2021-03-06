package br.edu.ulbra.election.election.service;

import br.edu.ulbra.election.election.exception.GenericOutputException;
import br.edu.ulbra.election.election.input.v1.VoteInput;
import br.edu.ulbra.election.election.model.Election;
import br.edu.ulbra.election.election.model.Vote;
import br.edu.ulbra.election.election.output.v1.CandidateOutput;
import br.edu.ulbra.election.election.output.v1.GenericOutput;
import br.edu.ulbra.election.election.output.v1.VoterOutput;
import br.edu.ulbra.election.election.repository.ElectionRepository;
import br.edu.ulbra.election.election.repository.VoteProjection;
import br.edu.ulbra.election.election.repository.VoteRepository;
import feign.FeignException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class VoteService {

    private VoteRepository voteRepository;

    private ElectionRepository electionRepository;

    private CandidateService candidateService;

    private VoterService voterService;


    public GenericOutput createVote(VoteInput voteInput, String token) {
        authenticate(token, voteInput.getVoterId());
        Vote vote = new Vote();
        vote.setElection(validateInput(voteInput));
        vote.setVoterId(voteInput.getVoterId());

        if (voteInput.getCandidateNumber() != null) {
            vote.setBlankVote(false);
        } else {
            vote.setBlankVote(true);
        }

        CandidateOutput candidateOutput = isCandidate(voteInput.getCandidateNumber());
        if (Optional.ofNullable(candidateOutput.getId()).isPresent()){
            vote.setCandidateId(candidateOutput.getId());
            vote.setNullVote(false);
        }else if (voteInput.getCandidateNumber() != null){
            vote.setNullVote(true);
        }else {
            vote.setNullVote(false);
        }

        if (!voteRepository.findByElectionIdAndVoterId(vote.getElection().getId(), vote.getVoterId()).isPresent()){
            voteRepository.save(vote);
            return new GenericOutput("You have voted");
        }else {
            return new GenericOutput("You have already voted");
        }

    }

    public GenericOutput createMultipleVote(List<VoteInput> votes, String token) {
        votes.forEach(voteInput -> createVote(voteInput, token));
        return new GenericOutput("You have voted");
    }

    public Election validateInput(VoteInput voteInput) {

        if (voteInput.getVoterId() == null) {
            throw new GenericOutputException("VoterId Cannot be Null");
        }

        if (voteInput.getElectionId() == null){
            throw new GenericOutputException("ElectionId Cannot be Null");
        }

        isValidVoter(voteInput.getVoterId());
        return electionRepository.findById(voteInput.getElectionId()).orElseThrow(EntityNotFoundException::new);
    }

    public List<VoteProjection> byElectionAndCandidate(Long candidateId, Long electionId) {
        return voteRepository.findByElectionIdAndCandidateId(electionId, candidateId);
    }

    public List<VoteProjection> byCandidate(Long candidateId) {
        return voteRepository.findByCandidateId(candidateId);
    }

    private CandidateOutput isCandidate(Long candidateNumber){
        CandidateOutput candidateOutput = new CandidateOutput();
        try{
            candidateOutput = candidateService.getByNumber(candidateNumber);
        }catch (FeignException e){
            if (e.status() == 500) {
                throw new EntityNotFoundException("Invalid Candidate");
            }
        }
        return candidateOutput;
    }

    public GenericOutput hasVotes(Long voterId)
    {
        List<Vote> votes = voteRepository.getByVoterId(voterId);
        if(!votes.isEmpty())
            return new GenericOutput("response: true");
        return new GenericOutput("response: false");
    }

    private void isValidVoter(Long voterId){
        try {
            Optional.ofNullable(voterService.getById(voterId)).orElseThrow(()-> new EntityNotFoundException("Voter not found"));
        }catch (FeignException e){
            if (e.status() == 500) {
                throw new EntityNotFoundException("Invalid Voter");
            }
        }
    }

    private void authenticate(String token, Long voterId)
    {
        try {
            VoterOutput voterOutput = voterService.checkToken(token);

            if(!voterOutput.getId().equals(voterId))
            {
                throw new GenericOutputException("Unauthorized");
            }
        }catch (FeignException e){
            System.out.println(e.getMessage());
            throw new GenericOutputException("Unauthorized");
        }
    }

}
