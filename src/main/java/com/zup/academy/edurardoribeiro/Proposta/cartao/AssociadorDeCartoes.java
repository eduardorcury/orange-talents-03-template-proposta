package com.zup.academy.edurardoribeiro.Proposta.cartao;

import com.zup.academy.edurardoribeiro.Proposta.criacao.Proposta;
import com.zup.academy.edurardoribeiro.Proposta.criacao.PropostaRepository;
import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.CacheManager;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import static com.zup.academy.edurardoribeiro.Proposta.criacao.StatusProposta.ELEGIVEL;

@Component
@EnableScheduling
public class AssociadorDeCartoes {

    private final PropostaRepository propostaRepository;
    private final CartaoRepository cartaoRepository;
    private final CartaoClient cartaoClient;
    private final CacheManager cacheManager;
    private final Logger logger = LoggerFactory.getLogger("jsonLogger");

    public AssociadorDeCartoes(PropostaRepository propostaRepository,
                               CartaoRepository cartaoRepository,
                               CartaoClient cartaoClient,
                               CacheManager cacheManager) {
        this.propostaRepository = propostaRepository;
        this.cartaoRepository = cartaoRepository;
        this.cartaoClient = cartaoClient;
        this.cacheManager = cacheManager;
    }

    @Scheduled(fixedRate = 2000, initialDelay = 5000)
    public void associaCartoes() {
        propostaRepository.findByStatus(ELEGIVEL).forEach(proposta -> {
            Long propostaId = proposta.getId();
            try {
                ConsultaCartaoResponse response = cartaoClient.consultaCartao(propostaId);
                associaCartaoAProposta(proposta, response);
            } catch (FeignException exception) {
                logger.info("Cartão para a proposta {} ainda não foi criado", propostaId);
            }
        });
    }

    public void associaCartaoAProposta(Proposta proposta, ConsultaCartaoResponse response) {

        Cartao cartao = response.toModel(proposta);
        cartaoRepository.save(cartao);
        proposta.associaCartao(cartao);
        propostaRepository.save(proposta);
        cacheManager.getCache("propostasElegiveis").clear();
        logger.info("Proposta de ID {} atrelada ao cartão de ID {}",
                proposta.getId(), response.retornaCartaoOfuscado());

    }
}
