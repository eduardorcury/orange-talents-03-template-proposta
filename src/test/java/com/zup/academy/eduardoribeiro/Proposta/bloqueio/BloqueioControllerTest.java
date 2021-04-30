package com.zup.academy.eduardoribeiro.Proposta.bloqueio;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zup.academy.eduardoribeiro.Proposta.cartao.Cartao;
import com.zup.academy.eduardoribeiro.Proposta.utils.TestUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class BloqueioControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper mapper;

    @Autowired
    BloqueioRepository bloqueioRepository;

    @Autowired
    TestUtils utils;

    @Test
    @DisplayName("Deve bloquear cartão para request válido")
    void deveBloquearCartaoExistente() throws Exception {

        Cartao cartao = utils.salvaCartao();
        NovoBloqueioRequest request = new NovoBloqueioRequest("1.1.1.1");

        mockMvc.perform(post("/cartoes/"+ cartao.getId() +"/bloqueio")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(request))
                .header("User-Agent", "User Agent"))
                .andExpect(status().isOk());

        assertTrue(cartao.bloqueado());
        assertThat(bloqueioRepository.findAll(), hasSize(1));

    }

    @Test
    @DisplayName("Não deve bloquear para request sem Header User-Agent")
    void naoDeveBloquearCartaoParaRequestSemHeader() throws Exception {

        Cartao cartao = utils.salvaCartao();
        NovoBloqueioRequest request = new NovoBloqueioRequest("1.1.1.1");

        mockMvc.perform(post("/cartoes/"+ cartao.getId() +"/bloqueio")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

    }

    @ParameterizedTest
    @ValueSource(strings = { "", "Corno fã de radiohead", "1.1.1" })
    @DisplayName("Não deve bloquear para request com IP inválido")
    void naoDeveBloquearCartaoParaRequestComIpInvalido(String ip) throws Exception {

        Cartao cartao = utils.salvaCartao();
        NovoBloqueioRequest request = new NovoBloqueioRequest(ip);

        mockMvc.perform(post("/cartoes/"+ cartao.getId() +"/bloqueio")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

    }

    @Test
    @DisplayName("Não deve bloquear cartão não encontrado")
    void naoDeveBloquearCartaoNaoEncontrado() throws Exception {

        NovoBloqueioRequest request = new NovoBloqueioRequest("1.1.1.1");

        mockMvc.perform(post("/cartoes/999/bloqueio")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(request))
                .header("User-Agent", "User Agent"))
                .andExpect(status().isNotFound());

    }

    @Test
    @DisplayName("Não deve bloquear cartão já bloqueado")
    void naoDeveBloquearCartaoJaBloqueado() throws Exception {

        Cartao cartao = utils.salvaCartaoBloqueado();
        NovoBloqueioRequest request = new NovoBloqueioRequest("1.1.1.1");

        mockMvc.perform(post("/cartoes/"+ cartao.getId() +"/bloqueio")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(request))
                .header("User-Agent", "User Agent"))
                .andExpect(status().isUnprocessableEntity());

    }
}