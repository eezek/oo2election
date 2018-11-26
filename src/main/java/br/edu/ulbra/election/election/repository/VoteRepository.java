package br.edu.ulbra.election.election.repository;

import br.edu.ulbra.election.election.model.Vote;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VoteRepository extends CrudRepository<Vote, Long> {

    Vote findByElectionIdAndVoterId(Long electionId, Long voterId);

    List<Vote> getByVoterId(Long voterId);

    List<Vote> findByElectionId(Long electionId);

    List<VoteProjection> findByCandidateId(Long candidateId);

    List<VoteProjection> findByElectionIdAndCandidateId(Long electionId, Long candidateId);

}
