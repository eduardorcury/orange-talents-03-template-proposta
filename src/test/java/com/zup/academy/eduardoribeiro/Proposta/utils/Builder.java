package com.zup.academy.eduardoribeiro.Proposta.utils;

import com.zup.academy.eduardoribeiro.Proposta.cartao.ConsultaCartaoResponse;
import com.zup.academy.eduardoribeiro.Proposta.criacao.NovaPropostaRequest;
import com.zup.academy.eduardoribeiro.Proposta.viagem.NovoAvisoRequest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;

public class Builder {

    public static NovaPropostaBuilder novaProposta() {
        return new NovaPropostaBuilder();
    }

    public static ConsultaCartaoBuilder novaConsultaCartao() {
        return new ConsultaCartaoBuilder();
    }

    public static NovoAvisoBuilder novoAviso() {
        return new NovoAvisoBuilder();
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

    public static class NovoAvisoBuilder {

        private String destino = "Dourados";
        private String ip = "1.1.1.1";
        private LocalDate termino = LocalDate.of(LocalDate.now().getYear() + 1, Month.FEBRUARY, 3);

        public NovoAvisoBuilder semDestino() {
            this.destino = null;
            return this;
        }

        public NovoAvisoBuilder semIp() {
            this.ip = null;
            return this;
        }

        public NovoAvisoBuilder ipInvalido() {
            this.ip = "1";
            return this;
        }

        public NovoAvisoBuilder semTermino() {
            this.termino = null;
            return this;
        }

        public NovoAvisoBuilder terminoInvalido() {
            this.termino = LocalDate.of(LocalDate.now().getYear() -1, Month.FEBRUARY, 3);
            return this;
        }

        public NovoAvisoRequest build() {
            return new NovoAvisoRequest(this.destino, this.termino, this.ip);
        }

    }

}
