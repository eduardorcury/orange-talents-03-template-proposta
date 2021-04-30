package com.zup.academy.eduardoribeiro.Proposta.bloqueio;

import com.zup.academy.eduardoribeiro.Proposta.cartao.Cartao;
import com.zup.academy.eduardoribeiro.Proposta.cartao.CartaoClient;
import com.zup.academy.eduardoribeiro.Proposta.cartao.CartaoRepository;
import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import static org.springframework.http.HttpStatus.*;

@Component
@EnableAsync
public class BloqueadorDeCartoes {

    private final BloqueioRepository bloqueioRepository;
    private final CartaoRepository cartaoRepository;
    private final CartaoClient cartaoClient;
    private final Logger logger = LoggerFactory.getLogger("jsonLogger");

    public BloqueadorDeCartoes(BloqueioRepository bloqueioRepository,
                               CartaoRepository cartaoRepository,
                               CartaoClient cartaoClient) {
        this.bloqueioRepository = bloqueioRepository;
        this.cartaoRepository = cartaoRepository;
        this.cartaoClient = cartaoClient;
    }

    public void bloqueiaCartao(Cartao cartao, String userAgent, NovoBloqueioRequest request) {

        if (cartao.bloqueado()) {
            throw new ResponseStatusException(UNPROCESSABLE_ENTITY);
        }

        Bloqueio bloqueio = request.toModel(userAgent, cartao);
        bloqueioRepository.save(bloqueio);
        notificaBloqueio(cartao);
        logger.info("Bloqueio criado para o cartão de ID {}", cartao.getId());

    }

    @Async
    private void notificaBloqueio(Cartao cartao) {

        NotificacaoBloqueio notificacao = new NotificacaoBloqueio();
        try {
            cartaoClient.notificaBloqueio(cartao.getIdExterno(), notificacao);
            cartao.bloquearCartao();
            cartaoRepository.save(cartao);
        } catch (FeignException exception) {
            logger.info("Notificação de bloqueio do cartão {} não foi aceita",
                    cartao.retornaCartaoOfuscado());
        }

    }

}
