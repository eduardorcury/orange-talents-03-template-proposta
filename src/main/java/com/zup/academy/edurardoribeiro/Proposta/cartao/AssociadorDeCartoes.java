package com.zup.academy.edurardoribeiro.Proposta.cartao;

import com.zup.academy.edurardoribeiro.Proposta.criacao.Proposta;
import com.zup.academy.edurardoribeiro.Proposta.criacao.PropostaRepository;
import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Component
@EnableScheduling
public class AssociadorDeCartoes {

    private final PropostaRepository propostaRepository;
    private final CartaoClient cartaoClient;
    private final Logger logger = LoggerFactory.getLogger("jsonLogger");

    public static List<Proposta> propostasElegiveis = new ArrayList<>();

    public AssociadorDeCartoes(PropostaRepository propostaRepository,
                               CartaoClient cartaoClient) {
        this.propostaRepository = propostaRepository;
        this.cartaoClient = cartaoClient;
    }

    @Scheduled(fixedRate = 5000, initialDelay = 10000)
    public void associaCartoes() {

        Iterator<Proposta> iterator = propostasElegiveis.iterator();
        while (iterator.hasNext()) {
            Proposta proposta = iterator.next();
            Long propostaId = proposta.getId();
            try {
                ConsultaCartaoResponse response = cartaoClient.consultaCartao(propostaId);
                proposta.associaCartao(response.getId());
                propostaRepository.save(proposta);
                logger.info("Proposta de ID {} atrelada ao cartão de ID {}", propostaId, response.retornaCartaoOfuscado());
                iterator.remove();
            } catch (FeignException exception) {
                logger.info("{} proposta(s) sem cartão atrelado", propostasElegiveis.size());
            }
        }

    }
}
