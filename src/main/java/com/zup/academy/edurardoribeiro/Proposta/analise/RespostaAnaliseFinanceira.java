package com.zup.academy.edurardoribeiro.Proposta.analise;

import com.zup.academy.edurardoribeiro.Proposta.criacao.StatusProposta;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class RespostaAnaliseFinanceira {

    private final Logger logger = LoggerFactory.getLogger("jsonLogger");

    @NotBlank
    private String documento;

    @NotBlank
    private String nome;

    @NotBlank
    private ResultadoAnalise resultadoSolicitacao;

    @NotNull
    private Long idProposta;

    public RespostaAnaliseFinanceira(@NotBlank String documento,
                                     @NotBlank String nome,
                                     @NotBlank ResultadoAnalise resultadoSolicitacao,
                                     @NotNull Long idProposta) {
        this.documento = documento;
        this.nome = nome;
        this.resultadoSolicitacao = resultadoSolicitacao;
        this.idProposta = idProposta;

        logger.info("Resposta de Análise Financeira da proposta {} recebida, resultado {}",
                idProposta, resultadoSolicitacao.toString());

    }

    public StatusProposta retornaStatusProposta() {
        return this.resultadoSolicitacao.retornaStatus();
    }

}
