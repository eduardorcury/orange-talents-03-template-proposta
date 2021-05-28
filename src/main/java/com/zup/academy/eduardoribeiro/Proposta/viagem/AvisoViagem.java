package com.zup.academy.eduardoribeiro.Proposta.viagem;

import com.zup.academy.eduardoribeiro.Proposta.cartao.Cartao;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "avisos")
public class AvisoViagem {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;

    @Column(nullable = false)
    private String destino;

    @Column(nullable = false)
    private LocalDate termino;

    @Column(nullable = false)
    private String ip;

    @Column(nullable = false)
    private String userAgent;

    @CreationTimestamp
    private LocalDateTime criadoEm;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Cartao cartao;

    public AvisoViagem(String destino,
                       LocalDate termino,
                       String ip,
                       String userAgent,
                       Cartao cartao) {
        this.destino = destino;
        this.termino = termino;
        this.ip = ip;
        this.userAgent = userAgent;
        this.cartao = cartao;
    }
}
