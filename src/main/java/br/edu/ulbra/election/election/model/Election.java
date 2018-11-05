package br.edu.ulbra.election.election.model;

import lombok.Data;

import javax.persistence.*;

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

}
