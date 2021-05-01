package com.zup.academy.eduardoribeiro.Proposta.bloqueio;

import com.zup.academy.eduardoribeiro.Proposta.cartao.Cartao;
import com.zup.academy.eduardoribeiro.Proposta.cartao.CartaoClient;
import com.zup.academy.eduardoribeiro.Proposta.utils.TestUtils;
import feign.FeignException;
import feign.Request;
import feign.RequestTemplate;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.nio.charset.StandardCharsets;
import java.util.Collections;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class BloqueadorDeCartoesTest {

    @Autowired
    BloqueadorDeCartoes bloqueadorDeCartoes;

    @Autowired
    TestUtils utils;

    @MockBean
    CartaoClient cartaoClient;

    private final String userAgent = "User Agent";
    private final NovoBloqueioRequest request = new NovoBloqueioRequest("1.1.1.1");
    private final NotificacaoBloqueio notificacao = new NotificacaoBloqueio();

    @Test
    @DisplayName("Deve bloquear cartão quando sistema legado retornar OK")
    void deveBloquearCartaoENotificarSistemaLegado() {

        Cartao cartao = utils.salvaCartao();

        when(cartaoClient.notificaBloqueio(cartao.getIdExterno(), notificacao))
                .thenReturn("Bloqueado");

        bloqueadorDeCartoes.bloqueiaCartao(cartao, userAgent, request);
        assertThat(cartao.bloqueado(), is(true));

    }

    @Test
    @DisplayName("Não deve bloquear cartão quando sistema legado retornar 4xx ou 5xx")
    void naoDeveBloquearCartao() {

        Cartao cartao = utils.salvaCartao();

        FeignException.BadRequest exception = new FeignException.BadRequest("sistema legado falhou",
                Request.create(Request.HttpMethod.POST, "URL", Collections.emptyMap(),
                        "teste".getBytes(), StandardCharsets.UTF_8, new RequestTemplate()), "teste".getBytes());

        when(cartaoClient.notificaBloqueio(cartao.getIdExterno(), notificacao)).thenThrow(exception);

        bloqueadorDeCartoes.bloqueiaCartao(cartao, userAgent, request);
        assertThat(cartao.bloqueado(), is(false));

    }

    @Test
    @DisplayName("Não deve bloquear cartão já bloqueado")
    void naoDeveBloquearCartaoJaBloqueado() {

        Cartao cartao = utils.salvaCartaoBloqueado();

        assertThrows(ResponseStatusException.class, () ->
                bloqueadorDeCartoes.bloqueiaCartao(cartao, userAgent, request));

    }
}