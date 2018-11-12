package br.edu.ulbra.election.election.service;

import br.edu.ulbra.election.election.input.v1.VoteInput;
import br.edu.ulbra.election.election.model.Vote;
import br.edu.ulbra.election.election.output.v1.GenericOutput;
import br.edu.ulbra.election.election.repository.VoteRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class VoteService {

    private VoteRepository voteRepository;

    private ElectionService electionService;

    private ModelMapper modelMapper;

    public GenericOutput createVote(VoteInput voteInput) {
        electionService.getById(voteInput.getElectionId());
        Vote vote = modelMapper.map(voteInput, Vote.class);
        this.create(vote);
        return new GenericOutput("You have voted");
    }

    public GenericOutput createMultipleVote(List<VoteInput> votes) {
        votes.forEach(voteInput -> create(modelMapper.map(voteInput, Vote.class)));
        return new GenericOutput("You have voted");
    }

    private void create(Vote vote) {
        voteRepository.save(vote);
    }

}
