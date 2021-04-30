package com.zup.academy.eduardoribeiro.Proposta.utils;

import com.zup.academy.eduardoribeiro.Proposta.cartao.Cartao;
import com.zup.academy.eduardoribeiro.Proposta.cartao.CartaoRepository;
import com.zup.academy.eduardoribeiro.Proposta.criacao.Proposta;
import com.zup.academy.eduardoribeiro.Proposta.criacao.PropostaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile("test")
@Component
public class TestUtils {

    @Autowired
    PropostaRepository propostaRepository;

    @Autowired
    CartaoRepository cartaoRepository;

    private Proposta salvaProposta() {
        Proposta proposta = Builder.novaProposta().build().toModel();
        return propostaRepository.save(proposta);
    }

    public Cartao salvaCartao() {
        Proposta proposta = Builder.novaProposta().build().toModel();
        propostaRepository.save(proposta);
        Cartao cartao = Builder.novaConsultaCartao()
                .comProposta(proposta.getId())
                .build().toModel(proposta);
        return cartaoRepository.save(cartao);
    }

    public Cartao salvaCartaoBloqueado() {
        Proposta proposta = Builder.novaProposta().build().toModel();
        propostaRepository.save(proposta);
        Cartao cartao = Builder.novaConsultaCartao()
                .comProposta(proposta.getId())
                .build().toModel(proposta);
        cartao.bloquearCartao();
        return cartaoRepository.save(cartao);
    }

}
