package br.edu.ulbra.election.election.api.v1;

import br.edu.ulbra.election.election.input.v1.VoteInput;
import br.edu.ulbra.election.election.output.v1.GenericOutput;
import br.edu.ulbra.election.election.repository.VoteProjection;
import br.edu.ulbra.election.election.service.VoteService;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/vote")
@AllArgsConstructor
public class VoteApi {

    private VoteService voteService;

    @PutMapping("/")
    @ApiOperation(value = "Vote")
    public GenericOutput electionVote(@RequestBody VoteInput voteInput) {
        return voteService.createVote(voteInput);
    }

    @PutMapping("/multiple")
    @ApiOperation(value = "Multiple Vote")
    public GenericOutput multipleElectionVote(@RequestBody List<VoteInput> voteInputList) {
        return voteService.createMultipleVote(voteInputList);
    }

    @GetMapping("/candidate/{candidateId}/election/{electionId}")
    @ApiOperation(value = "Get Vote by Election and Candidate")
    public List<VoteProjection> byElectionAndCandidate(@PathVariable Long candidateId, @PathVariable Long electionId) {
        return voteService.byElectionAndCandidate(candidateId, electionId);
    }

    @GetMapping("/candidate/{candidateId}")
    @ApiOperation(value = "get Vote by Candidate")
    public List<VoteProjection> byCandidate(@PathVariable Long candidateId) {
        return voteService.byCandidate(candidateId);
    }
}
