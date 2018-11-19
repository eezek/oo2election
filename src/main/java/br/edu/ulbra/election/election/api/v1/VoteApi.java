package br.edu.ulbra.election.election.api.v1;

import br.edu.ulbra.election.election.input.v1.VoteInput;
import br.edu.ulbra.election.election.model.Vote;
import br.edu.ulbra.election.election.output.v1.GenericOutput;
import br.edu.ulbra.election.election.service.VoteService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/vote")
@AllArgsConstructor
public class VoteApi {

    private VoteService voteService;

    @PutMapping("/")
    public GenericOutput electionVote(@RequestBody VoteInput voteInput) {
        return voteService.createVote(voteInput);
    }

    @PutMapping("/multiple")
    public GenericOutput multipleElectionVote(@RequestBody List<VoteInput> voteInputList) {
        return voteService.createMultipleVote(voteInputList);
    }

    @GetMapping("/candidate/{candidateId}/election/{electionId}")
    public List<Vote> byElectionAndCandidate(@PathVariable Long candidateId, @PathVariable Long electionId){
        return voteService.byElectionAndCandidate(candidateId, electionId);
    }

    @GetMapping("/candidate/{candidateId}")
    public List<Vote> byCandidate(@PathVariable Long candidateId){
        return voteService.byCandidate(candidateId);
    }
}
