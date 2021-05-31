package com.zup.academy.eduardoribeiro.Proposta.carteira;

import com.zup.academy.eduardoribeiro.Proposta.cartao.Cartao;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "carteiras")
public class Carteira {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;

    @ManyToOne(optional = false)
    private Cartao cartao;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private TipoDeCarteira tipo;

    public Carteira(Cartao cartao, String email, TipoDeCarteira tipo) {
        this.cartao = cartao;
        this.email = email;
        this.tipo = tipo;
    }

    public String getId() {
        return id;
    }

    public Cartao getCartao() {
        return cartao;
    }
}
