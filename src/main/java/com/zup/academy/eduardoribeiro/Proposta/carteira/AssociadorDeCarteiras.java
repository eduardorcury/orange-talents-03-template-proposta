package com.zup.academy.eduardoribeiro.Proposta.carteira;

import com.zup.academy.eduardoribeiro.Proposta.cartao.Cartao;
import com.zup.academy.eduardoribeiro.Proposta.cartao.CartaoClient;
import feign.FeignException;
import io.opentracing.Tracer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Component
public class AssociadorDeCarteiras {

    private final CarteiraRepository carteiraRepository;
    private final CartaoClient cartaoClient;
    private final Tracer tracer;
    private final Logger LOGGER = LoggerFactory.getLogger("jsonLogger");

    public AssociadorDeCarteiras(CarteiraRepository carteiraRepository,
                                 CartaoClient cartaoClient,
                                 Tracer tracer) {
        this.carteiraRepository = carteiraRepository;
        this.cartaoClient = cartaoClient;
        this.tracer = tracer;
    }

    public ResponseEntity<?> associaCarteira(Cartao cartao,
                                             AssociacaoCarteiraRequest request,
                                             UriComponentsBuilder uriBuilder) {

        tracer.activeSpan().setTag("cartao.id", cartao.getId());

        if (carteiraRepository.existsByCartaoAndTipo(cartao, TipoDeCarteira.valueOf(request.getTipo()))) {
            LOGGER.error("Tentativa de associar carteira de tipo {} já existente ao cartão de id {}",
                    request.getTipo(),
                    cartao.getId());
            return ResponseEntity.unprocessableEntity().build();
        }

        try {
            cartaoClient.associaCarteira(cartao.getIdExterno(), new NotificacaoCarteira(request));
            Carteira carteira = request.toModel(cartao);
            carteiraRepository.save(carteira);
            URI uri = uriBuilder.path("/cartoes/{cartaoId}/carteiras/{carteiraId}")
                    .buildAndExpand(cartao.getId(), carteira.getId())
                    .toUri();
            LOGGER.info("Carteira de id {} associada ao cartão de id {}", carteira.getId(), cartao.getId());
            return ResponseEntity.created(uri).build();
        } catch (FeignException exception) {
            LOGGER.error("Sistema bancário retornou erro no request de carteira {}", request);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }

}
