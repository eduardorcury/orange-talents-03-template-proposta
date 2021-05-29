package com.zup.academy.eduardoribeiro.Proposta.carteira;

import java.util.Objects;

public class NotificacaoCarteira {

    private String email;
    private String carteira = "PAYPAL";

    public NotificacaoCarteira(AssociacaoCarteiraRequest request) {
        this.email = request.getEmail();
    }

    public String getEmail() {
        return email;
    }

    public String getCarteira() {
        return carteira;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NotificacaoCarteira that = (NotificacaoCarteira) o;
        return Objects.equals(email, that.email) && Objects.equals(carteira, that.carteira);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email, carteira);
    }
}
