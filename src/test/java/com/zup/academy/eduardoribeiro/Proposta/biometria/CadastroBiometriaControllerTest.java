package com.zup.academy.eduardoribeiro.Proposta.biometria;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zup.academy.eduardoribeiro.Proposta.cartao.Cartao;
import com.zup.academy.eduardoribeiro.Proposta.cartao.CartaoRepository;
import com.zup.academy.eduardoribeiro.Proposta.criacao.Proposta;
import com.zup.academy.eduardoribeiro.Proposta.criacao.PropostaRepository;
import com.zup.academy.eduardoribeiro.Proposta.utils.TestUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static com.zup.academy.eduardoribeiro.Proposta.utils.Builder.novaConsultaCartao;
import static com.zup.academy.eduardoribeiro.Proposta.utils.Builder.novaProposta;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class CadastroBiometriaControllerTest {

    private final String url = "/cartoes/1111-2222-3333-4444/biometrias";

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper mapper;

    @Autowired
    TestUtils utils;

    @Test
    void deveCadastrarBiometriaParaCartaoExistente() throws Exception {

        Cartao cartao = utils.salvaCartao();
        CadastroBiometriaRequest request = new CadastroBiometriaRequest("ZmluZ2VycHJpbnR2YWxpZG8=");

        mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsBytes(request)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "http://localhost" + url + "/1"));
    }

    @ParameterizedTest
    @ValueSource(strings = { "", "ab cd" })
    void deveRetornarBadRequestParaBiometriaInvalida(String fingerprint) throws Exception {

        CadastroBiometriaRequest request = new CadastroBiometriaRequest(fingerprint);

        mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

    }

    @Test
    void deveRetornarNotFoundParaCartaoNaoExistente() throws Exception {

        CadastroBiometriaRequest request = new CadastroBiometriaRequest("ZmluZ2VycHJpbnR2YWxpZG8=");

        mockMvc.perform(post("/cartoes/9999/biometrias")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());

    }
}