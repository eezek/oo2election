package br.edu.ulbra.election.election.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@RequiredArgsConstructor
@Entity
@Table(name = "vote")
public class Vote implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long voterId;

    @Column(nullable = true)
    private Long candidateId;

    @Column(name = "election_id", insertable = false, updatable = false)
    private Long electionId;

    @Column(nullable = false)
    private Boolean blankVote;

    @Column(nullable = false)
    private Boolean nullVote;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Election election;

}
