package br.edu.ulbra.election.election.repository;

import br.edu.ulbra.election.election.model.Election;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ElectionRepository extends CrudRepository<Election, Long> {

    List<Election> findByYear(Integer year);
}
