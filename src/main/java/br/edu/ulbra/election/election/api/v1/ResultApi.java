package br.edu.ulbra.election.election.api.v1;

import br.edu.ulbra.election.election.output.v1.ElectionCandidateResultOutput;
import br.edu.ulbra.election.election.output.v1.GenericOutput;
import br.edu.ulbra.election.election.output.v1.ResultOutput;
import br.edu.ulbra.election.election.service.ResultService;
import br.edu.ulbra.election.election.service.VoteService;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/result")
@AllArgsConstructor
public class ResultApi {

    private VoteService voteService;
    private ResultService resultService;

    @GetMapping("/election/{electionId}")
    @ApiOperation(value = "Get Result by Election")
    public ResultOutput getResultByElection(@PathVariable Long electionId) {
        return new ResultOutput();
    }

    @GetMapping("/candidate/{candidateId}")
    @ApiOperation(value = "Get Result by Candidate")
    public ElectionCandidateResultOutput getResultByCandidate(@PathVariable Long candidateId) {
        return resultService.getResultByCandidate(candidateId);
    }

    @GetMapping("/voter/{voterId}")
    public GenericOutput hasVotes(@PathVariable Long voterId) {
        return voteService.hasVotes(voterId);
    }
}
