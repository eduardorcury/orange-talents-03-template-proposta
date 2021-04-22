package com.zup.academy.edurardoribeiro.Proposta.builder;

import com.zup.academy.edurardoribeiro.Proposta.criacao.NovaPropostaRequest;

import java.math.BigDecimal;

public class CriadorRequests {

    public static NovaPropostaBuilder builder() {
        return new NovaPropostaBuilder();
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

        public NovaPropostaRequest build() {
            return new NovaPropostaRequest(this.documento, this.email,
                    this.nome, this.endereco, this.salario);
        }

    }

}
