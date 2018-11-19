package br.edu.ulbra.election.election.service;

import br.edu.ulbra.election.election.input.v1.VoteInput;
import br.edu.ulbra.election.election.model.Election;
import br.edu.ulbra.election.election.model.Vote;
import br.edu.ulbra.election.election.output.v1.CandidateOutput;
import br.edu.ulbra.election.election.output.v1.GenericOutput;
import br.edu.ulbra.election.election.repository.ElectionRepository;
import br.edu.ulbra.election.election.repository.VoteRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
@AllArgsConstructor
public class VoteService {

    private VoteRepository voteRepository;

    private ElectionRepository electionRepository;

    private CandidateService candidateService;

    public GenericOutput createVote(VoteInput voteInput) {

        Vote vote = new Vote();
        vote.setElection(validateInput(voteInput));
        vote.setVoterId(voteInput.getVoterId());

        if (voteInput.getCandidateNumber() == null) {
            vote.setBlankVote(true);
        } else {
            vote.setBlankVote(false);
        }

        // TODO: Validate null candidate
        vote.setNullVote(false);

        voteRepository.save(vote);

        return new GenericOutput("You have voted");
    }

    public GenericOutput createMultipleVote(List<VoteInput> votes) {
        votes.forEach(voteInput -> createVote(voteInput));
        return new GenericOutput("You have voted");
    }

    public Election validateInput(VoteInput voteInput) {

        if (voteInput.getVoterId() == null) {
            throw new EntityNotFoundException("Invalid Voter");
        }
        // TODO: Validate voter
        return electionRepository.findById(voteInput.getElectionId()).orElseThrow(EntityNotFoundException::new);
    }

    public List<Vote> byElectionAndCandidate(Long candidateId, Long electionId) {
        return voteRepository.findByElectionIdAndCandidateId(electionId, candidateId);
    }

    public List<Vote> byCandidate(Long candidateId) {
        return voteRepository.findByCandidateId(candidateId);
    }

    private CandidateOutput visCandidate(Long candiadteNumber){
        return candidateService.getByNumber(candiadteNumber);
    }

}
