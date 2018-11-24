package br.edu.ulbra.election.election.repository;

import br.edu.ulbra.election.election.model.Vote;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VoteRepository extends CrudRepository<Vote, Long> {

    List<Vote> getByVoterId(Long voterId);

    List<Vote> findByElectionId(Long electionId);

    List<Vote> findByCandidateId(Long candidateId);

    List<Vote> findByElectionIdAndCandidateId(Long electionId, Long candidateId);

}
