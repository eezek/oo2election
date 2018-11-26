package br.edu.ulbra.election.election.service;

import br.edu.ulbra.election.election.exception.GenericOutputException;
import br.edu.ulbra.election.election.input.v1.VoteInput;
import br.edu.ulbra.election.election.model.Election;
import br.edu.ulbra.election.election.model.Vote;
import br.edu.ulbra.election.election.output.v1.CandidateOutput;
import br.edu.ulbra.election.election.output.v1.GenericOutput;
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

    public GenericOutput createVote(VoteInput voteInput) {

        Vote vote = new Vote();
        vote.setElection(validateInput(voteInput));
        vote.setVoterId(voteInput.getVoterId());

        if (voteInput.getCandidateNumber() == null) {
            vote.setBlankVote(true);
        } else {
            vote.setBlankVote(false);
        }

        CandidateOutput candidateOutput = isCandidate(voteInput.getCandidateNumber());
        if (candidateOutput != null){
            vote.setCandidateId(candidateOutput.getId());
            vote.setNullVote(false);
        }else {
            vote.setNullVote(true);
        }

        if (voteRepository.findByElectionIdAndVoterId(vote.getElectionId(), vote.getVoterId()) != null){
            voteRepository.save(vote);
            return new GenericOutput("You have voted");
        }else {
            return new GenericOutput("You have already voted");
        }

    }

    public GenericOutput createMultipleVote(List<VoteInput> votes) {
        votes.forEach(voteInput -> createVote(voteInput));
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

}
