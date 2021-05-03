package com.zup.academy.eduardoribeiro.Proposta.cartao;

import com.zup.academy.eduardoribeiro.Proposta.biometria.Biometria;
import com.zup.academy.eduardoribeiro.Proposta.criacao.Proposta;
import com.zup.academy.eduardoribeiro.Proposta.viagem.AvisoViagem;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "cartoes")
public class Cartao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String idExterno;

    @Column(nullable = false)
    private String titular;

    @Column(nullable = false)
    private LocalDateTime emitidoEm;

    @OneToOne(optional = false, mappedBy = "cartao", fetch = FetchType.LAZY)
    private Proposta proposta;

    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "cartao", fetch = FetchType.LAZY)
    private List<Biometria> biometrias;

    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "cartao", fetch = FetchType.LAZY)
    private List<AvisoViagem> avisos;

    private Boolean bloqueado = false;

    @Deprecated
    public Cartao() {

    }

    public Cartao(String idExterno, String titular, LocalDateTime emitidoEm, Proposta proposta) {
        this.idExterno = idExterno;
        this.titular = titular;
        this.emitidoEm = emitidoEm;
        this.proposta = proposta;
    }

    public Long getId() {
        return id;
    }

    public String getIdExterno() {
        return idExterno;
    }

    public String retornaCartaoOfuscado() {
        StringBuilder builder = new StringBuilder(this.idExterno);
        builder.replace(7, 13, "** ***");
        return builder.toString();
    }

    public Boolean bloqueado() {
        return this.bloqueado;
    }

    public void bloquearCartao() {
        this.bloqueado = true;
    }

}
