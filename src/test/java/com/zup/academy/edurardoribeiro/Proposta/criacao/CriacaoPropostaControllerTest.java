package com.zup.academy.edurardoribeiro.Proposta.criacao;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zup.academy.edurardoribeiro.Proposta.analise.AnaliseFinanceiraService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.stream.Stream;

import static com.zup.academy.edurardoribeiro.Proposta.builder.Builder.novaProposta;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class CriacaoPropostaControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper mapper;

    @MockBean
    AnaliseFinanceiraService service;

    @Test
    @DisplayName("Criação de proposta válida")
    void criacaoDePropostaValida() throws Exception {

        NovaPropostaRequest requestValido = novaProposta().build();

        mockMvc.perform(post("/propostas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(requestValido)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "http://localhost/propostas/1"));

        // Para um novo request com o mesmo documento, deve retornar Erro 422
        mockMvc.perform(post("/propostas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(requestValido)))
                .andExpect(status().isUnprocessableEntity());

    }

    @ParameterizedTest
    @MethodSource("criaPropostasInvalidas")
    @DisplayName("Criação de propostas inválidas")
    void criacaoDePropostaInvalida(NovaPropostaRequest request) throws Exception {

        mockMvc.perform(post("/propostas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

    }

    private static Stream<NovaPropostaRequest> criaPropostasInvalidas() {
        return Stream.of(
                novaProposta().semDocumento().build(),
                novaProposta().documentoInvalido().build(),
                novaProposta().semNome().build(),
                novaProposta().semEmail().build(),
                novaProposta().emailInvalido().build(),
                novaProposta().semEndereco().build(),
                novaProposta().semSalario().build(),
                novaProposta().salarioNegativo().build()
        );
    }
}