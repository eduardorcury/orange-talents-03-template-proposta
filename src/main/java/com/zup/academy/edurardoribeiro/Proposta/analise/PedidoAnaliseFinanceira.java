package com.zup.academy.edurardoribeiro.Proposta.analise;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zup.academy.edurardoribeiro.Proposta.criacao.Proposta;
import com.zup.academy.edurardoribeiro.Proposta.criacao.PropostaRepository;
import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.IOException;

public class PedidoAnaliseFinanceira {

    private final Logger logger = LoggerFactory.getLogger("jsonLogger");

    @NotBlank
    private String documento;

    @NotBlank
    private String nome;

    @NotNull
    private Long idProposta;

    public PedidoAnaliseFinanceira(@NotNull Proposta proposta) {

        this.documento = proposta.getDocumento();
        this.nome = proposta.getNome();
        this.idProposta = proposta.getId();

        logger.info("Pedido de análise financeira da proposta {} criado", idProposta);
    }

    public String getDocumento() {
        return documento;
    }

    public String getNome() {
        return nome;
    }

    public Long getIdProposta() {
        return idProposta;
    }
}
