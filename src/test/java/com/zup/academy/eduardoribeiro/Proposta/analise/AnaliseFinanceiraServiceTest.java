package com.zup.academy.eduardoribeiro.Proposta.analise;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zup.academy.eduardoribeiro.Proposta.utils.Builder;
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
import java.util.concurrent.Callable;

import static com.zup.academy.eduardoribeiro.Proposta.criacao.StatusProposta.ELEGIVEL;
import static com.zup.academy.eduardoribeiro.Proposta.criacao.StatusProposta.NAO_ELEGIVEL;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
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
        pedido = new PedidoAnaliseFinanceira(proposta);
    }

    @Test
    @DisplayName("Analisa proposta que retorna consulta sem restrição")
    void analisaPropostaSemRestricao() throws IOException, InterruptedException {

        RespostaAnaliseFinanceira resposta = new RespostaAnaliseFinanceira(
                request.getDocumento(),
                request.getNome(),
                ResultadoAnalise.SEM_RESTRICAO, proposta.getId());

        when(client.consulta(pedido)).thenReturn(resposta);
        service.analise(proposta);
        //await().until(proposta::getStatus, equalTo(ELEGIVEL));
        assertThat(proposta.getStatus(), is(ELEGIVEL));

    }

    @Test
    @DisplayName("Analisa proposta que retorna consulta com restrição")
    void analisaPropostaComRestricao() throws IOException, InterruptedException {

        RespostaAnaliseFinanceira resposta = new RespostaAnaliseFinanceira(
                request.getDocumento(),
                request.getNome(),
                ResultadoAnalise.COM_RESTRICAO, proposta.getId());

        FeignException.UnprocessableEntity exception =
                new FeignException.UnprocessableEntity(
                        "Erro 422 da analise",
                        Request.create(Request.HttpMethod.POST, "url", Collections.emptyMap(),
                                mapper.writeValueAsBytes(request), Charset.defaultCharset(), new RequestTemplate()),
                        mapper.writeValueAsBytes(resposta));


        when(client.consulta(pedido)).thenThrow(exception);
        service.analise(proposta);
        //await().until(proposta::getStatus, equalTo(NAO_ELEGIVEL));
        assertThat(proposta.getStatus(), is(NAO_ELEGIVEL));
        Thread.sleep(1000);

    }

}