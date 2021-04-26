package com.zup.academy.edurardoribeiro.Proposta.cartao;

import com.zup.academy.edurardoribeiro.Proposta.builder.CriadorRequests;
import com.zup.academy.edurardoribeiro.Proposta.criacao.Proposta;
import com.zup.academy.edurardoribeiro.Proposta.criacao.StatusProposta;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("testeintegracao")
class AssociadorDeCartoesTest {
    
    @Autowired
    AssociadorDeCartoes associadorDeCartoes;

    @MockBean
    CartaoClient cartaoClient;

    Proposta proposta;

    @BeforeEach
    void setUp() {
        proposta = CriadorRequests.builder().build().toModel();
        proposta.setStatus(StatusProposta.ELEGIVEL);
        ReflectionTestUtils.setField(proposta, "id", 1L);
        AssociadorDeCartoes.propostasElegiveis.add(proposta);
    }

    @Test
    @DisplayName("Deve associar cartão a proposta")
    void deveAssociarCartaoAProposta() {
        ConsultaCartaoResponse response = new ConsultaCartaoResponse("id-cartao");
        when(cartaoClient.consultaCartao(1L)).thenReturn(response);

        associadorDeCartoes.associaCartoes();

        assertThat(proposta.getStatus(), is(StatusProposta.CARTAO_ATRELADO));
        assertThat(proposta.getCartaoId(), is(response.getId()));
        assertThat(AssociadorDeCartoes.propostasElegiveis, empty());
    }
}