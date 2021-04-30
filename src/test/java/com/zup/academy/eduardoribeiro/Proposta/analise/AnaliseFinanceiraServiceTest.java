package com.zup.academy.eduardoribeiro.Proposta.analise;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zup.academy.eduardoribeiro.Proposta.builder.Builder;
import com.zup.academy.eduardoribeiro.Proposta.criacao.NovaPropostaRequest;
import com.zup.academy.eduardoribeiro.Proposta.criacao.Proposta;
import feign.FeignException;
import feign.Request;
import feign.RequestTemplate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Collections;

import static com.zup.academy.eduardoribeiro.Proposta.criacao.StatusProposta.ELEGIVEL;
import static com.zup.academy.eduardoribeiro.Proposta.criacao.StatusProposta.NAO_ELEGIVEL;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class AnaliseFinanceiraServiceTest {

    @Autowired
    AnaliseFinanceiraService service;

    @Autowired
    ObjectMapper mapper;

    @MockBean
    AnaliseFinanceiraClient client;

    NovaPropostaRequest request;
    Proposta proposta;
    PedidoAnaliseFinanceira pedido;

    @BeforeEach
    void setUp() {
        request = Builder.novaProposta().build();
        proposta = request.toModel();
        ReflectionTestUtils.setField(proposta,"id", 1L);
        pedido = new PedidoAnaliseFinanceira(proposta);
    }

    @Test
    @DisplayName("Analisa proposta que retorna consulta sem restrição")
    void analisaPropostaSemRestricao() throws IOException {

        RespostaAnaliseFinanceira resposta = new RespostaAnaliseFinanceira(
                request.getDocumento(),
                request.getNome(),
                ResultadoAnalise.SEM_RESTRICAO, 1L);

        when(client.consulta(pedido)).thenReturn(resposta);
        service.analise(proposta);
        await().until(() -> proposta.getStatus() != null);
        assertThat(proposta.getStatus(), is(ELEGIVEL));

    }

    @Test
    @DisplayName("Analisa proposta que retorna consulta com restrição")
    void analisaPropostaComRestricao() throws IOException {

        RespostaAnaliseFinanceira resposta = new RespostaAnaliseFinanceira(
                request.getDocumento(),
                request.getNome(),
                ResultadoAnalise.COM_RESTRICAO, 1L);

        FeignException.UnprocessableEntity exception =
                new FeignException.UnprocessableEntity(
                        "Erro 422 da analise",
                        Request.create(Request.HttpMethod.POST, "url", Collections.emptyMap(),
                                mapper.writeValueAsBytes(request), Charset.defaultCharset(), new RequestTemplate()),
                        mapper.writeValueAsBytes(resposta));


        when(client.consulta(pedido)).thenThrow(exception);
        service.analise(proposta);
        await().until(() -> proposta.getStatus() != null);
        assertThat(proposta.getStatus(), is(NAO_ELEGIVEL));

    }
}