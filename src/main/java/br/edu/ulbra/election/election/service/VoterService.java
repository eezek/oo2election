package br.edu.ulbra.election.election.service;

import br.edu.ulbra.election.election.model.Vote;
import br.edu.ulbra.election.election.output.v1.GenericOutput;
import br.edu.ulbra.election.election.repository.VoteRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@AllArgsConstructor
public class VoterService {

    private VoteRepository voteRepository;

    public GenericOutput hasVotes(Long voterId)
    {
        List<Vote> votes = voteRepository.getByVoterId(voterId);
        if(votes.size()>0)
            return new GenericOutput("response: true");
        return new GenericOutput("response: false");
    }

}
