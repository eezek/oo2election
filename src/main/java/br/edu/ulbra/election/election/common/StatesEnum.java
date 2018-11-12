package br.edu.ulbra.election.election.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum StatesEnum {
    RO("RO"),
    AC("AC"),
    AM("AM"),
    RR("RR"),
    PA("PA"),
    AP("AP"),
    TO("TO"),
    MA("MA"),
    PI("PI"),
    CE("CE"),
    RN("RN"),
    PB("PB"),
    PE("PE"),
    AL("AL"),
    SE("SE"),
    BA("BA"),
    MG("MG"),
    ES("ES"),
    RJ("RJ"),
    SP("SP"),
    PR("PR"),
    SC("SC"),
    RS("RS"),
    MS("MS"),
    MT("MT"),
    GO("GO"),
    DF("DF"),
    BR("BR");

    private final String stateCode;

}
