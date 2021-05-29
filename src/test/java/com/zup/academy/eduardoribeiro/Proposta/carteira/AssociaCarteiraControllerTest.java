package com.zup.academy.eduardoribeiro.Proposta.carteira;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zup.academy.eduardoribeiro.Proposta.cartao.Cartao;
import com.zup.academy.eduardoribeiro.Proposta.cartao.CartaoClient;
import com.zup.academy.eduardoribeiro.Proposta.utils.TestUtils;
import feign.FeignException;
import feign.Request;
import feign.RequestTemplate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class AssociaCarteiraControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper mapper;

    @Autowired
    CarteiraRepository carteiraRepository;

    @Autowired
    TestUtils utils;

    @MockBean
    CartaoClient cartaoClient;

    @Test
    @DisplayName("Deve associar uma nova carteira a um cartão existente")
    void deveAssociarCarteiraACartao() throws Exception {

        Cartao cartao = utils.salvaCartao();

        AssociacaoCarteiraRequest request = new AssociacaoCarteiraRequest("email@gmail.com");

        mockMvc.perform(post("/cartoes/" + cartao.getId() + "/carteiras")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"));

        List<Carteira> list = carteiraRepository.findAll();
        assertEquals(1, list.size());
        assertEquals(cartao, list.get(0).getCartao());

    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = { "email", "  " })
    @DisplayName("Deve retornar BAD_REQUEST para request inválido")
    void naoDeveAssociarCarteiraParaRequestInvalido(String email) throws Exception {

        Cartao cartao = utils.salvaCartao();

        AssociacaoCarteiraRequest request = new AssociacaoCarteiraRequest(email);

        mockMvc.perform(post("/cartoes/" + cartao.getId() + "/carteiras")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        assertTrue(carteiraRepository.findAll().isEmpty());

    }

    @Test
    @DisplayName("Deve retornar NOT_FOUND para cartão não encontrado")
    void deveRetornarNotFoundParaCartaoNaoEncontrado() throws Exception {

        AssociacaoCarteiraRequest request = new AssociacaoCarteiraRequest("email@gmail.com");

        mockMvc.perform(post("/cartoes/1/carteiras")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());

        assertTrue(carteiraRepository.findAll().isEmpty());

    }

    @Test
    @DisplayName("Deve retornar UNPROCESSABLE_ENTITY para carteira já associada")
    void deveRetornarUnprocessableEntityParaCarteiraJaAssociada() throws Exception {

        Cartao cartao = utils.salvaCartao();
        carteiraRepository.save(new Carteira(cartao, "email@gmail.com"));

        assertEquals(1, carteiraRepository.findAll().size());

        AssociacaoCarteiraRequest request = new AssociacaoCarteiraRequest("email@gmail.com");

        mockMvc.perform(post("/cartoes/" + cartao.getId() + "/carteiras")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(request)))
                .andExpect(status().isUnprocessableEntity());

        assertEquals(1, carteiraRepository.findAll().size());

    }

    @Test
    @DisplayName("Deve retornar INTERNAL_SERVER_ERROR se sistema retornar erro")
    void deveRetornarInternalServerErrorSeSistemaRetornarErro() throws Exception {

        Cartao cartao = utils.salvaCartao();
        AssociacaoCarteiraRequest request = new AssociacaoCarteiraRequest("email@gmail.com");
        FeignException.BadRequest exception =
                new FeignException.BadRequest(
                        "Erro do sistema",
                        Request.create(Request.HttpMethod.POST, "url", Collections.emptyMap(),
                                mapper.writeValueAsBytes(request), Charset.defaultCharset(), new RequestTemplate()),
                        mapper.writeValueAsBytes("resposta"));

        Mockito.when(cartaoClient.associaCarteira(cartao.getIdExterno(),
                new NotificacaoCarteira(request))).thenThrow(exception);

        mockMvc.perform(post("/cartoes/" + cartao.getId() + "/carteiras")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(request)))
                .andExpect(status().isInternalServerError());

        assertTrue(carteiraRepository.findAll().isEmpty());
    }
}