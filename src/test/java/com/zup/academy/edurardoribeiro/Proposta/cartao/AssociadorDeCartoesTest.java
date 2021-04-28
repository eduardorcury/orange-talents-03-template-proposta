package com.zup.academy.edurardoribeiro.Proposta.cartao;

import com.zup.academy.edurardoribeiro.Proposta.builder.CriadorRequests;
import com.zup.academy.edurardoribeiro.Proposta.criacao.Proposta;
import com.zup.academy.edurardoribeiro.Proposta.criacao.PropostaRepository;
import com.zup.academy.edurardoribeiro.Proposta.criacao.StatusProposta;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.zup.academy.edurardoribeiro.Proposta.criacao.StatusProposta.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
class AssociadorDeCartoesTest {
    
    @Autowired
    AssociadorDeCartoes associadorDeCartoes;

    @Autowired
    PropostaRepository repository;

    @MockBean
    CartaoClient cartaoClient;

    @Test
    @DisplayName("Deve associar cartão a proposta")
    @Transactional
    void deveAssociarCartaoAProposta() {

        Proposta proposta = CriadorRequests.builder().build().toModel();
        proposta.setStatus(ELEGIVEL);
        repository.save(proposta);
        List<Proposta> byStatus = repository.findByStatus(ELEGIVEL);
        ConsultaCartaoResponse response = new ConsultaCartaoResponse("id-cartao");
        when(cartaoClient.consultaCartao(1L)).thenReturn(response);

        associadorDeCartoes.associaCartoes();

        assertThat(proposta.getStatus(), is(CARTAO_ATRELADO));
        assertThat(proposta.getCartaoId(), is(response.getId()));
    }
}