package com.zup.academy.edurardoribeiro.Proposta.cartao;

import com.zup.academy.edurardoribeiro.Proposta.biometria.Biometria;
import com.zup.academy.edurardoribeiro.Proposta.criacao.Proposta;

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

    @Deprecated
    public Cartao() {

    }

    public Cartao(String idExterno, String titular, LocalDateTime emitidoEm, Proposta proposta) {
        this.idExterno = idExterno;
        this.titular = titular;
        this.emitidoEm = emitidoEm;
        this.proposta = proposta;
    }

    public String retornaCartaoOfuscado() {
        StringBuilder builder = new StringBuilder(this.idExterno);
        builder.replace(7, 13, "** ***");
        return builder.toString();
    }

}