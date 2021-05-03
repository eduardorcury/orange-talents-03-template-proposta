package com.zup.academy.eduardoribeiro.Proposta.cartao;

import com.zup.academy.eduardoribeiro.Proposta.criacao.PropostaRepository;
import com.zup.academy.eduardoribeiro.Proposta.criacao.Proposta;
import feign.FeignException;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tags;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.DependsOn;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

import static com.zup.academy.eduardoribeiro.Proposta.criacao.StatusProposta.ELEGIVEL;

@Component
@EnableScheduling
@DependsOn("metricas")
public class AssociadorDeCartoes {

    private final String appNome;

    private Counter cartaoAtrelado;

    private final PropostaRepository propostaRepository;
    private final CartaoRepository cartaoRepository;
    private final CartaoClient cartaoClient;
    private final CacheManager cacheManager;
    private final MeterRegistry meterRegistry;
    private final Logger logger = LoggerFactory.getLogger("jsonLogger");

    public AssociadorDeCartoes(@Value("${app.nome}") String appNome,
                               PropostaRepository propostaRepository,
                               CartaoRepository cartaoRepository,
                               CartaoClient cartaoClient,
                               CacheManager cacheManager,
                               MeterRegistry meterRegistry) {
        this.appNome = appNome;
        this.propostaRepository = propostaRepository;
        this.cartaoRepository = cartaoRepository;
        this.cartaoClient = cartaoClient;
        this.cacheManager = cacheManager;
        this.meterRegistry = meterRegistry;
    }

    @PostConstruct
    private void init() {
        cartaoAtrelado = this.meterRegistry
                .get(appNome + ".criadas")
                .tag("estado", "cartao_atrelado")
                .counter();
    }

    @Scheduled(fixedRate = 2000, initialDelay = 5000)
    public void associaCartoes() {

        List<Proposta> propostasElegiveis = propostaRepository.findByStatus(ELEGIVEL);
        this.meterRegistry.gaugeCollectionSize(appNome + ".criadas.total",
                Tags.of("estado", "elegivel"), propostasElegiveis);

        propostasElegiveis.forEach(proposta -> {
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
        this.cartaoAtrelado.increment();

    }
}
