package br.edu.ulbra.election.election.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
public class ElectionVoter implements Serializable {

    @EmbeddedId
    @AttributeOverrides({@AttributeOverride(name = "electionId", column = @Column(name = "election_id")),
            @AttributeOverride(name = "voterId", column = @Column(name = "voter_id", nullable = false))})
    private ElectionVoterId id;

}
