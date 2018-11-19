package br.edu.ulbra.election.election.output.v1;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(description = "Candidate Output Information")
@Data
public class CandidateOutput {

    @ApiModelProperty(example = "1", notes = "Candidate Unique Identification")
    private Long id;
    @ApiModelProperty(example = "John Doe", notes = "Candidate name")
    private String name;
    @ApiModelProperty(example = "77654", notes = "Candidate Election Number")
    private Long numberElection;
    @ApiModelProperty(notes = "Candidate Party Data")
    private PartyOutput partyOutput;

}
