package br.edu.ulbra.election.election.service;

import lombok.AllArgsConstructor;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Service
@AllArgsConstructor
public class VoterService {

    private VoterClient candidateClient;

    public Object getById(Long id) {
        return this.candidateClient.getById(id);
    }

    @FeignClient(value = "voter-service", url = "${url.voter-service}")
    private interface VoterClient {

        @GetMapping("/v1/voter/{voterId}")
        Object getById(@PathVariable(name = "voterId") Long voterId);

    }
}
