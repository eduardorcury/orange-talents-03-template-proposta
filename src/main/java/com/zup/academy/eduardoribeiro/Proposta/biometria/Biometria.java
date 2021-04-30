package com.zup.academy.eduardoribeiro.Proposta.biometria;

import com.zup.academy.eduardoribeiro.Proposta.cartao.Cartao;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "biometrias")
public class Biometria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreationTimestamp
    private LocalDateTime criadaEm;

    @Column(nullable = false)
    private String fingerprint;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Cartao cartao;

    @Deprecated
    public Biometria() {

    }

    public Biometria(String fingerprint, Cartao cartao) {
        this.fingerprint = fingerprint;
        this.cartao = cartao;
    }

    public Long getId() {
        return id;
    }
}
