package br.edu.ulbra.election.election.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Set;

@Data
@Entity
public class Election {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer year;

    @Column(nullable = false)
    private String stateCode;

    @Column(nullable = false)
    private String description;

    @OneToMany(mappedBy = "election")
    private Set<Vote> voters;

}
