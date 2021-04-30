package com.zup.academy.eduardoribeiro.Proposta.cartao;

import com.zup.academy.eduardoribeiro.Proposta.criacao.PropostaRepository;
import com.zup.academy.eduardoribeiro.Proposta.criacao.Proposta;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static com.zup.academy.eduardoribeiro.Proposta.builder.Builder.novaConsultaCartao;
import static com.zup.academy.eduardoribeiro.Proposta.builder.Builder.novaProposta;
import static com.zup.academy.eduardoribeiro.Proposta.criacao.StatusProposta.CARTAO_ATRELADO;
import static com.zup.academy.eduardoribeiro.Proposta.criacao.StatusProposta.ELEGIVEL;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class AssociadorDeCartoesTest {
    
    @Autowired
    AssociadorDeCartoes associadorDeCartoes;

    @Autowired
    PropostaRepository repository;

    @Autowired
    CartaoRepository cartaoRepository;

    @MockBean
    CartaoClient cartaoClient;

    @Test
    @DisplayName("Deve associar cartão a proposta")
    void deveAssociarCartaoAProposta() {

        Proposta proposta = novaProposta().build().toModel();
        proposta.setStatus(ELEGIVEL);
        repository.save(proposta);
        ConsultaCartaoResponse response = novaConsultaCartao().build();
        when(cartaoClient.consultaCartao(1L)).thenReturn(response);

        associadorDeCartoes.associaCartoes();

        assertThat(proposta.getStatus(), is(CARTAO_ATRELADO));
        assertThat(proposta.getCartao(), is(cartaoRepository.getOne(1L)));
        assertThat(cartaoRepository.findAll(), hasSize(1));

    }
}