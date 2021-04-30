package com.zup.academy.eduardoribeiro.Proposta.criacao;

import com.zup.academy.eduardoribeiro.Proposta.cartao.Cartao;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Entity
@Table(name = "propostas")
public class Proposta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String documento;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private String endereco;

    @Column(nullable = false)
    private BigDecimal salario;

    @Enumerated(EnumType.STRING)
    private StatusProposta status;

    @OneToOne
    @JoinColumn(name = "idCartao")
    private Cartao cartao;

    @Deprecated
    public Proposta() {

    }

    public Proposta(String documento, String email, String nome, String endereco, BigDecimal salario) {
        this.documento = documento;
        this.email = email;
        this.nome = nome;
        this.endereco = endereco;
        this.salario = salario;
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public BigDecimal getSalario() {
        return salario;
    }

    public String getDocumento() {
        return documento;
    }

    public StatusProposta getStatus() {
        return status;
    }

    public Cartao getCartao() {
        return cartao;
    }

    public void setStatus(StatusProposta status) {
        this.status = status;
    }

    public String retornaDocumentoOfuscado() {
        StringBuilder builder = new StringBuilder(this.documento);
        builder.replace(4, this.documento.length(), "***.***-**");
        return builder.toString();
    }

    public void associaCartao(@NotNull Cartao cartao) {
        this.cartao = cartao;
        this.status = StatusProposta.CARTAO_ATRELADO;
    }

}
