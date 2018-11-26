package br.edu.ulbra.election.election.service;

import br.edu.ulbra.election.election.output.v1.ElectionCandidateResultOutput;
import br.edu.ulbra.election.election.output.v1.ElectionOutput;
import br.edu.ulbra.election.election.output.v1.ResultOutput;
import br.edu.ulbra.election.election.repository.VoteProjection;
import br.edu.ulbra.election.election.repository.VoteRepository;
import feign.FeignException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ResultService {

    private VoteRepository voteRepository;
    private CandidateService candidateService;
    private ElectionService electionService;

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

    public ResultOutput getElectionResults(Long electionId) {

        ResultOutput resultOutput = new ResultOutput();

        ElectionOutput electionOutput = electionService.getById(electionId);

        resultOutput.setElection(electionOutput);

        List<VoteProjection> votes = voteRepository.findByElectionIdAndCandidateIdNotNull(electionId);
        Optional.ofNullable(votes).orElseThrow(EntityNotFoundException::new);

        List<Long> groupCandidateIdList = votes.stream()
                .filter(distinctByKey(vote -> vote.getCandidateId()))
                .map(VoteProjection::getCandidateId)
                .collect(Collectors.toList());

        List<ElectionCandidateResultOutput> electionCandidateResultOutputList = new ArrayList<>();

        groupCandidateIdList.forEach(candidate -> electionCandidateResultOutputList.add(getResultByCandidate(candidate)));

        resultOutput.setCandidates(electionCandidateResultOutputList);

        resultOutput.setBlankVotes(voteRepository.getResultsByBlankVote(true));

        resultOutput.setNullVotes(voteRepository.getResultsByNullVote(true));

        resultOutput.setTotalVotes(voteRepository.getResultsByElectionId(electionId));

        return resultOutput;
    }

    private static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Map<Object, Boolean> seen = new ConcurrentHashMap<>();
        return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }


}
