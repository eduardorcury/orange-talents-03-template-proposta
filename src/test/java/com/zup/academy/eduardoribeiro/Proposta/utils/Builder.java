package com.zup.academy.eduardoribeiro.Proposta.utils;

import com.zup.academy.eduardoribeiro.Proposta.cartao.ConsultaCartaoResponse;
import com.zup.academy.eduardoribeiro.Proposta.criacao.NovaPropostaRequest;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Builder {

    public static NovaPropostaBuilder novaProposta() {
        return new NovaPropostaBuilder();
    }

    public static ConsultaCartaoBuilder novaConsultaCartao() {
        return new ConsultaCartaoBuilder();
    }

    public static class NovaPropostaBuilder {

        private String documento = "688.128.100-55";
        private String nome = "Eduardo";
        private String email = "eduardo@gmail.com";
        private String endereco = "Endereço";
        private BigDecimal salario = new BigDecimal(5000);

        public NovaPropostaBuilder semDocumento() {
            this.documento = null;
            return this;
        }

        public NovaPropostaBuilder documentoInvalido() {
            this.documento = "123";
            return this;
        }

        public NovaPropostaBuilder semNome() {
            this.nome = " ";
            return this;
        }

        public NovaPropostaBuilder semEmail() {
            this.email = null;
            return this;
        }

        public NovaPropostaBuilder emailInvalido() {
            this.email = "teste";
            return this;
        }

        public NovaPropostaBuilder semEndereco() {
            this.endereco = " ";
            return this;
        }

        public NovaPropostaBuilder semSalario() {
            this.salario = null;
            return this;
        }

        public NovaPropostaBuilder salarioNegativo() {
            this.salario = new BigDecimal(-5000);
            return this;
        }

        public NovaPropostaBuilder comRestricao() {
            this.documento = "383.708.230-09";
            return this;
        }

        public NovaPropostaRequest build() {
            return new NovaPropostaRequest(this.documento, this.email,
                    this.nome, this.endereco, this.salario);
        }

    }

    public static class ConsultaCartaoBuilder {

        private String id = "1111-2222-3333-4444";
        private String titular = "Eduardo";
        private LocalDateTime emitidoEm = LocalDateTime.now();
        private String idProposta = "1";

        public ConsultaCartaoBuilder comProposta(Long idProposta) {
            this.idProposta = idProposta.toString();
            return this;
        }

        public ConsultaCartaoResponse build() {
            return new ConsultaCartaoResponse(this.id, this.titular,
                    this.emitidoEm, this.idProposta);
        }

    }
}
