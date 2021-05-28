package com.zup.academy.eduardoribeiro.Proposta.viagem;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zup.academy.eduardoribeiro.Proposta.cartao.Cartao;
import com.zup.academy.eduardoribeiro.Proposta.cartao.CartaoClient;
import com.zup.academy.eduardoribeiro.Proposta.utils.TestUtils;
import feign.FeignException;
import feign.Request;
import feign.RequestTemplate;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.Charset;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static com.zup.academy.eduardoribeiro.Proposta.utils.Builder.novoAviso;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class AvisoViagemControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    TestUtils utils;

    @Autowired
    ObjectMapper mapper;

    @Autowired
    AvisoViagemRepository repository;

    @MockBean
    CartaoClient cartaoClient;

    @Test
    @DisplayName("Deve criar aviso de viagem para request válido")
    void deveCriarAvisoViagemParaRequestValido() throws Exception {

        Cartao cartao = utils.salvaCartao();
        NovoAvisoRequest request = novoAviso().build();

        when(cartaoClient.notificaViagem(cartao.getIdExterno(),
                new NotificacaoViagem(request)))
                .thenReturn("ok");

        mockMvc.perform(post("/cartoes/" + cartao.getId() + "/avisos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(request))
                .header("User-Agent", "userAgent"))
                .andExpect(status().isOk());

        List<AvisoViagem> list = repository.findAll();
        assertEquals(1, list.size());
        assertEquals(cartao, list.get(0).getCartao());

    }

    @Test
    @DisplayName("Não deve criar aviso de viagem se o sistema retornar diferente de 200")
    void naoDeveCriarAvisoViagemSeSistemaRetornarErro() throws Exception {

        Cartao cartao = utils.salvaCartao();
        NovoAvisoRequest request = novoAviso().build();
        FeignException.BadRequest exception =
                new FeignException.BadRequest(
                        "Erro do sistema",
                        Request.create(Request.HttpMethod.POST, "url", Collections.emptyMap(),
                                mapper.writeValueAsBytes(request), Charset.defaultCharset(), new RequestTemplate()),
                        mapper.writeValueAsBytes("resposta"));


        when(cartaoClient.notificaViagem(cartao.getIdExterno(), new NotificacaoViagem(request)))
                .thenThrow(exception);

        mockMvc.perform(post("/cartoes/" + cartao.getId() + "/avisos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(request))
                .header("User-Agent", "userAgent"))
                .andExpect(status().isInternalServerError());

        assertTrue(repository.findAll().isEmpty());

    }

    @Test
    @DisplayName("Deve retornar erro 404 para cartão não encontrado")
    void deveRetornarNotFoundParaCartaoNaoEncontrado() throws Exception {

        NovoAvisoRequest request = novoAviso().build();

        mockMvc.perform(post("/cartoes/999/avisos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(request))
                .header("User-Agent", "userAgent"))
                .andExpect(status().isNotFound());

    }

    @Test
    @DisplayName("Deve retornar erro 400 para request sem Header User-Agent")
    void deveRetornarBadRequestParaRequestSemHeader() throws Exception {

        Cartao cartao = utils.salvaCartao();
        NovoAvisoRequest request = novoAviso().build();

        mockMvc.perform(post("/cartoes/" + cartao.getId() + "/avisos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

    }

    @ParameterizedTest
    @MethodSource("criaRequestInvalidos")
    @DisplayName("Deve retornar erro 400 para request inválido")
    void deveRetornarBadRequestParaRequestInvalido(NovoAvisoRequest request) throws Exception {

        Cartao cartao = utils.salvaCartao();

        mockMvc.perform(post("/cartoes/" + cartao.getId() + "/avisos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(request))
                .header("User-Agent", "userAgent"))
                .andExpect(status().isBadRequest());

    }

    private static Stream<NovoAvisoRequest> criaRequestInvalidos() {
        return Stream.of(
                novoAviso().semDestino().build(),
                novoAviso().semIp().build(),
                novoAviso().ipInvalido().build(),
                novoAviso().semTermino().build(),
                novoAviso().terminoInvalido().build()
        );
    }

}