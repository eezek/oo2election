package br.edu.ulbra.election.election.service;

import br.edu.ulbra.election.election.output.v1.ElectionCandidateResultOutput;
import br.edu.ulbra.election.election.repository.VoteProjection;
import br.edu.ulbra.election.election.repository.VoteRepository;
import feign.FeignException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
@AllArgsConstructor
public class ResultService {

    private VoteRepository voteRepository;
    private CandidateService candidateService;

    public ElectionCandidateResultOutput getResultByCandidate(Long candidateId) {
        ElectionCandidateResultOutput electionCandidateResultOutput = new ElectionCandidateResultOutput();
        try {
            electionCandidateResultOutput.setCandidate(candidateService.getById(candidateId));
        } catch (FeignException e) {
            if (e.status() == 500) {
                throw new EntityNotFoundException("Invalid Candidate");
            }
        }

        List<VoteProjection> votes = voteRepository.findByCandidateId(candidateId);
        electionCandidateResultOutput.setTotalVotes(Long.valueOf(votes.size()));

        return electionCandidateResultOutput;
    }

}
