package br.edu.ulbra.election.election.service;

import br.edu.ulbra.election.election.input.v1.ElectionInput;
import br.edu.ulbra.election.election.model.Election;
import br.edu.ulbra.election.election.output.v1.ElectionOutput;
import br.edu.ulbra.election.election.output.v1.GenericOutput;
import br.edu.ulbra.election.election.repository.ElectionRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.lang.reflect.Type;
import java.util.List;

@Service
@AllArgsConstructor
public class ElectionService {

    private ElectionRepository electionRepository;

    private ModelMapper modelMapper;

    private static final String MESSAGE_INVALID_ID = "Invalid id";
    private static final String MESSAGE_NOT_FOUND = "Not found";

    public List<ElectionOutput> getAll() {
        Type electionOutputListType = new TypeToken<List<ElectionOutput>>() {
        }.getType();
        return modelMapper.map(electionRepository.findAll(), electionOutputListType);
    }

    public List<ElectionOutput> getByYear(Integer year) {
        Type electionOutputListType = new TypeToken<List<ElectionOutput>>() {
        }.getType();
        return modelMapper.map(electionRepository.findByYear(year), electionOutputListType);
    }

    public ElectionOutput getById(Long electionId) {
        return modelMapper.map(byId(electionId), ElectionOutput.class);
    }

    public ElectionOutput create(ElectionInput electionInput) {
        Election election = modelMapper.map(electionInput, Election.class);
        election = electionRepository.save(election);
        return modelMapper.map(election, ElectionOutput.class);
    }

    public ElectionOutput update(Long electionId, ElectionInput electionInput) {

        Election election = byId(electionId);

        election.setStateCode(electionInput.getStateCode());
        election.setYear(electionInput.getYear());
        election.setDescription(electionInput.getDescription());
        election = electionRepository.save(election);
        return modelMapper.map(election, ElectionOutput.class);
    }

    public GenericOutput delete(Long electionId) {

        electionRepository.delete(byId(electionId));

        return new GenericOutput("Election deleted");

    }

    private Election byId(Long electionId) {
        return electionRepository.findById(electionId).orElseThrow(() -> new EntityNotFoundException(MESSAGE_NOT_FOUND));
    }


}
