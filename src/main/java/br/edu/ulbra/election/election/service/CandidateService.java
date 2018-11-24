package br.edu.ulbra.election.election.service;

import br.edu.ulbra.election.election.output.v1.CandidateOutput;
import lombok.AllArgsConstructor;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Service
@AllArgsConstructor
public class CandidateService {

    private CandidateClient candidateClient;

    public CandidateOutput getByElectionId(Long id) {
        return this.candidateClient.getByElectionId(id);
    }

    public CandidateOutput getByNumber(Long number) {
        return this.candidateClient.getByNumber(number);
    }

    public CandidateOutput getById (Long id) {return this.candidateClient.getById(id); }

    @FeignClient(value = "candidate-service", url = "${url.candidate-service}")
    private interface CandidateClient {

        @GetMapping("/v1/candidate/election/{electionId}")
        CandidateOutput getByElectionId(@PathVariable(name = "electionId") Long electionId);

        @GetMapping("/v1/candidate/number/{candidateNum}")
        CandidateOutput getByNumber(@PathVariable(name = "candidateNum") Long candidateNum);

        @GetMapping("/v1/candidate/{candidateId}")
        CandidateOutput getById(@PathVariable(name = "candidateId") Long candidateId);
    }
}
