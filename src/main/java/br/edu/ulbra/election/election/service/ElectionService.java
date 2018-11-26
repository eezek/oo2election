package br.edu.ulbra.election.election.service;

import br.edu.ulbra.election.election.common.StatesEnum;
import br.edu.ulbra.election.election.input.v1.ElectionInput;
import br.edu.ulbra.election.election.model.Election;
import br.edu.ulbra.election.election.output.v1.CandidateOutput;
import br.edu.ulbra.election.election.output.v1.ElectionOutput;
import br.edu.ulbra.election.election.output.v1.GenericOutput;
import br.edu.ulbra.election.election.repository.ElectionRepository;
import br.edu.ulbra.election.election.repository.VoteRepository;
import com.google.common.base.Enums;
import feign.FeignException;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ElectionService {

    private ElectionRepository electionRepository;

    private VoteRepository voteRepository;

    private CandidateService candidateService;

    private ModelMapper modelMapper;

    private static final String MESSAGE_INVALID_ID = "Invalid id";
    private static final String MESSAGE_STATE = "Invalid state input";
    private static final String MESSAGE_YEAR = "Invalid year input";
    private static final String MESSAGE_INVALID_DESC = "Invalid description input";
    private static final String MESSAGE_NOT_FOUND = "Election not found";

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
        isValidInput(electionInput);
        Election election = modelMapper.map(electionInput, Election.class);
        election = electionRepository.save(election);
        return modelMapper.map(election, ElectionOutput.class);
    }

    public ElectionOutput update(Long electionId, ElectionInput electionInput) {
        isValidInput(electionInput);
        verifyElection(electionId);
        Election election = byId(electionId);

        election.setStateCode(electionInput.getStateCode());
        election.setYear(electionInput.getYear());
        election.setDescription(electionInput.getDescription());
        election = electionRepository.save(election);
        return modelMapper.map(election, ElectionOutput.class);
    }

    public GenericOutput delete(Long electionId) {
        verifyElection(electionId);
        electionRepository.delete(byId(electionId));

        return new GenericOutput("Election deleted");

    }

    private Election byId(Long electionId) {
        return electionRepository.findById(electionId).orElseThrow(() -> new EntityNotFoundException(MESSAGE_NOT_FOUND));
    }

    private void verifyElection(Long electionId){
        Optional.ofNullable(voteRepository.findByElectionId(electionId)).ifPresent(x -> {
            if (!x.isEmpty()){
            throw new EntityNotFoundException("Election cannot be touched");}
        });

        try {
            CandidateOutput candidateOutput = candidateService.getByElectionId(electionId);
            if (candidateOutput != null) {
                throw new EntityNotFoundException("Election already have candidates");
            }
        } catch (FeignException e) {
            if (e.status() == 500) {
                throw new EntityNotFoundException("Cannot verify election candidates");
            }
        }

    }

    private void isValidInput(ElectionInput election) {
        if (!Enums.getIfPresent(StatesEnum.class, election.getStateCode().toUpperCase()).isPresent()) {
            throw new EntityNotFoundException(MESSAGE_STATE);
        }
        if (election.getYear() < 2000 || election.getYear() > 2200) {
            throw new EntityNotFoundException(MESSAGE_YEAR);
        }
        if (election.getDescription().length() < 5) {
            throw new EntityNotFoundException(MESSAGE_INVALID_DESC);
        }
    }

}
