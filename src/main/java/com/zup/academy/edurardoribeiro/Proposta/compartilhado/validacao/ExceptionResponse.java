package com.zup.academy.edurardoribeiro.Proposta.compartilhado.validacao;

public class ExceptionResponse {

    private String campo;

    private String mensagem;

    public ExceptionResponse(String campo, String mensagem) {
        this.campo = campo;
        this.mensagem = mensagem;
    }

    public String getCampo() {
        return campo;
    }

    public String getMensagem() {
        return mensagem;
    }
}
