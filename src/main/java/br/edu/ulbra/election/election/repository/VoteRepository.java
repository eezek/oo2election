package br.edu.ulbra.election.election.repository;

import br.edu.ulbra.election.election.model.Vote;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VoteRepository extends CrudRepository<Vote, Long> {

    Optional<Vote> findByElectionIdAndVoterId(Long electionId, Long voterId);

    List<Vote> getByVoterId(Long voterId);

    List<VoteProjection> findByElectionId(Long electionId);

    List<VoteProjection> findByElectionIdAndCandidateIdNotNull(Long electionId);

    @Query(" select count (v.candidateId) " +
            "from Vote v " +
            "where v.candidateId =:candidateId")
    Long getResultsByCandidateId(Long candidateId);

    @Query(" select count (v.nullVote) " +
            "from Vote v " +
            "where v.nullVote =:isNullVote")
    Long getResultsByNullVote(Boolean isNullVote);

    @Query(" select count (v.blankVote) " +
            "from Vote v " +
            "where v.blankVote =:isBlankVote")
    Long getResultsByBlankVote(Boolean isBlankVote);

    @Query(" select count (v.electionId) " +
            "from Vote v " +
            "where v.electionId =:electionId")
    Long getResultsByElectionId(Long electionId);


    List<VoteProjection> findByCandidateId(Long candidateId);

    List<VoteProjection> findByElectionIdAndCandidateId(Long electionId, Long candidateId);

}
