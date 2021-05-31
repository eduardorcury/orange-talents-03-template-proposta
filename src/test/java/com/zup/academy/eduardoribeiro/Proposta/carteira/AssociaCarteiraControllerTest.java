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
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
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
import java.util.stream.Stream;

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

    @ParameterizedTest
    @ValueSource(strings = {"PAYPAL", "SAMSUNG_PAY"})
    @DisplayName("Deve associar uma nova carteira a um cartão existente")
    void deveAssociarCarteiraACartao(String tipo) throws Exception {

        Cartao cartao = utils.salvaCartao();

        AssociacaoCarteiraRequest request = new AssociacaoCarteiraRequest("email@gmail.com", tipo);

        mockMvc.perform(post("/cartoes/" + cartao.getId() + "/carteiras")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"));

        List<Carteira> list = carteiraRepository.findAll();
        assertEquals(1, list.size());
        assertEquals(cartao, list.get(0).getCartao());

    }

    @Test
    @DisplayName("Deve associar duas carteiras diferentes a um cartão")
    void deveAssociarDuasCarteirasDiferentesACartao() throws Exception {

        Cartao cartao = utils.salvaCartao();

        AssociacaoCarteiraRequest request1 =
                new AssociacaoCarteiraRequest("email@gmail.com", "PAYPAL");

        AssociacaoCarteiraRequest request2 =
                new AssociacaoCarteiraRequest("email@gmail.com", "SAMSUNG_PAY");

        mockMvc.perform(post("/cartoes/" + cartao.getId() + "/carteiras")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(request1)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"));

        mockMvc.perform(post("/cartoes/" + cartao.getId() + "/carteiras")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(request2)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"));

        List<Carteira> list = carteiraRepository.findAll();
        assertEquals(2, list.size());
        assertEquals(cartao, list.get(0).getCartao());
        assertEquals(cartao, list.get(1).getCartao());

    }

    @ParameterizedTest
    @MethodSource("criaRequestInvalidos")
    @DisplayName("Deve retornar BAD_REQUEST para request inválido")
    void naoDeveAssociarCarteiraParaRequestInvalido(String email, String tipo) throws Exception {

        Cartao cartao = utils.salvaCartao();

        AssociacaoCarteiraRequest request = new AssociacaoCarteiraRequest(email, tipo);

        mockMvc.perform(post("/cartoes/" + cartao.getId() + "/carteiras")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        assertTrue(carteiraRepository.findAll().isEmpty());

    }

    @Test
    @DisplayName("Deve retornar NOT_FOUND para cartão não encontrado")
    void deveRetornarNotFoundParaCartaoNaoEncontrado() throws Exception {

        AssociacaoCarteiraRequest request = new AssociacaoCarteiraRequest("email@gmail.com", "PAYPAL");

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
        carteiraRepository.save(new Carteira(cartao, "email@gmail.com", TipoDeCarteira.PAYPAL));

        assertEquals(1, carteiraRepository.findAll().size());

        AssociacaoCarteiraRequest request = new AssociacaoCarteiraRequest("email@gmail.com", "PAYPAL");

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
        AssociacaoCarteiraRequest request = new AssociacaoCarteiraRequest("email@gmail.com", "PAYPAL");
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

    private static Stream<Arguments> criaRequestInvalidos() {
        return Stream.of(
                Arguments.of(null, "PAYPAL"),
                Arguments.of("email@gmail.com", null),
                Arguments.of(" ", " "),
                Arguments.of("email", "PAYPAL"),
                Arguments.of("email@gmail.com", "qualquer")
        );
    }

}