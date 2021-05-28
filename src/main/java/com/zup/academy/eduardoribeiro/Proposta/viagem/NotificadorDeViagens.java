package com.zup.academy.eduardoribeiro.Proposta.viagem;

import com.zup.academy.eduardoribeiro.Proposta.cartao.Cartao;
import com.zup.academy.eduardoribeiro.Proposta.cartao.CartaoClient;
import com.zup.academy.eduardoribeiro.Proposta.cartao.CartaoRepository;
import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class NotificadorDeViagens {

    private final AvisoViagemRepository avisoRepository;
    private final CartaoRepository cartaoRepository;
    private final CartaoClient client;
    private final Logger LOGGER = LoggerFactory.getLogger(NotificadorDeViagens.class);

    public NotificadorDeViagens(AvisoViagemRepository avisoRepository,
                                CartaoRepository cartaoRepository,
                                CartaoClient client) {
        this.avisoRepository = avisoRepository;
        this.cartaoRepository = cartaoRepository;
        this.client = client;
    }

    public ResponseEntity<?> notifica(Long cartaoId, NovoAvisoRequest request, String userAgent) {

        Optional<Cartao> possivelCartao = cartaoRepository.findById(cartaoId);

        if (possivelCartao.isPresent()) {
            Cartao cartao = possivelCartao.get();
            AvisoViagem aviso = request.toModel(cartao, userAgent);
            NotificacaoViagem notificacao = new NotificacaoViagem(request);
            try {
                client.notificaViagem(cartao.getIdExterno(), notificacao);
                avisoRepository.save(aviso);
                LOGGER.info("Aviso de Viagem criado para o cartão de ID {}", cartao.getId());
                return ResponseEntity.ok().build();
            } catch (FeignException exception) {
                LOGGER.error("Aviso de Viagem falhou para o cartão de ID {}", cartao.getId(), exception);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        } else {
            return ResponseEntity.notFound().build();
        }

    }

}
