package br.edu.ulbra.election.election.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "vote")
public class Vote implements Serializable {

    @EmbeddedId
    private VoteId id;

    @Column(name = "candidate_id", nullable = false)
    private Long candidateId;

    @ManyToOne(targetEntity = Election.class)
    @JoinColumn(name = "election_id", insertable = false, updatable = false)
    private Election election;

}
