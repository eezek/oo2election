package br.edu.ulbra.election.election.api.v1;

import br.edu.ulbra.election.election.input.v1.VoteInput;
import br.edu.ulbra.election.election.output.v1.GenericOutput;
import br.edu.ulbra.election.election.service.VoteService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
