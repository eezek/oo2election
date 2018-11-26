package br.edu.ulbra.election.election.repository;

public interface VoteProjection {

    Long getVoterId();

    Long getCandidateId();

    Long getElectionId();
}
