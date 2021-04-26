package com.zup.academy.edurardoribeiro.Proposta.cartao;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotBlank;

public class ConsultaCartaoResponse {

    @NotBlank
    private String id;

    @JsonCreator
    public ConsultaCartaoResponse(@NotBlank @JsonProperty("id") String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String retornaCartaoOfuscado() {
        StringBuilder builder = new StringBuilder(this.id);
        builder.replace(7, 13, "** ***");
        return builder.toString();
    }

}
