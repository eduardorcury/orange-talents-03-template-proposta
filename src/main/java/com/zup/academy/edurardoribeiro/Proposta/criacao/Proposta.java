package com.zup.academy.edurardoribeiro.Proposta.criacao;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
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

    private String cartaoId;

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

    public String getCartaoId() {
        return cartaoId;
    }

    public void setStatus(StatusProposta status) {
        this.status = status;
    }

    public String retornaDocumentoOfuscado() {
        StringBuilder builder = new StringBuilder(this.documento);
        builder.replace(4, this.documento.length(), "***.***-**");
        return builder.toString();
    }

    public void associaCartao(@NotBlank String cartaoId) {
        this.cartaoId = cartaoId;
        this.status = StatusProposta.CARTAO_ATRELADO;
    }

}
