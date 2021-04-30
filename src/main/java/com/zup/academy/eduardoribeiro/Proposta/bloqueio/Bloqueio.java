package com.zup.academy.eduardoribeiro.Proposta.bloqueio;

import com.zup.academy.eduardoribeiro.Proposta.cartao.Cartao;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.Valid;
import java.time.LocalDateTime;

@Entity
@Table(name = "bloqueios")
public class Bloqueio {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;

    @CreationTimestamp
    private LocalDateTime bloqueadoEm;

    @Column(nullable = false)
    private String enderecoIp;

    @Column(nullable = false)
    private String userAgent;

    @Valid
    @OneToOne(optional = false, fetch = FetchType.LAZY)
    private Cartao cartao;

    @Deprecated
    public Bloqueio() {

    }

    public Bloqueio(String enderecoIp, String userAgent, Cartao cartao) {
        this.enderecoIp = enderecoIp;
        this.userAgent = userAgent;
        this.cartao = cartao;
    }
}
