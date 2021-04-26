package com.zup.academy.edurardoribeiro.Proposta.acompanhamento;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zup.academy.edurardoribeiro.Proposta.builder.CriadorRequests;
import com.zup.academy.edurardoribeiro.Proposta.criacao.NovaPropostaRequest;
import com.zup.academy.edurardoribeiro.Proposta.criacao.Proposta;
import com.zup.academy.edurardoribeiro.Proposta.criacao.PropostaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("testeintegracao")
@Sql(statements = "INSERT INTO propostas (id, cartao_id, documento, email, endereco, nome, salario, status)" +
        " VALUES (1, 'cartao-id', '899.291.190-43', 'email@gmail.com', 'endereco', 'Eduardo', '5000', 'ELEGIVEL');")
class AcompanhamentoPropostaControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    void deveRetornarPropostaDeIdInformado() throws Exception {

        mockMvc.perform(get("/propostas/1"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/propostas/2"))
                .andExpect(status().isNotFound());

    }
}