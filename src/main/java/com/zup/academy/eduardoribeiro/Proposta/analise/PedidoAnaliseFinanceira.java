package com.zup.academy.eduardoribeiro.Proposta.analise;

import com.zup.academy.eduardoribeiro.Proposta.criacao.Proposta;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PedidoAnaliseFinanceira that = (PedidoAnaliseFinanceira) o;
        return Objects.equals(documento, that.documento) && Objects.equals(nome, that.nome) && Objects.equals(idProposta, that.idProposta);
    }

}
