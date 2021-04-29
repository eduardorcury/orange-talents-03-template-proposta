package com.zup.academy.edurardoribeiro.Proposta.acompanhamento;

import com.zup.academy.edurardoribeiro.Proposta.builder.Builder;
import com.zup.academy.edurardoribeiro.Proposta.criacao.NovaPropostaRequest;
import com.zup.academy.edurardoribeiro.Proposta.criacao.Proposta;
import com.zup.academy.edurardoribeiro.Proposta.criacao.PropostaRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class AcompanhamentoPropostaControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    PropostaRepository propostaRepository;

    @Test
    void deveRetornarPropostaDeIdInformado() throws Exception {

        Proposta proposta = Builder.novaProposta().build().toModel();
        propostaRepository.save(proposta);

        mockMvc.perform(get("/propostas/1"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/propostas/2"))
                .andExpect(status().isNotFound());

    }
}