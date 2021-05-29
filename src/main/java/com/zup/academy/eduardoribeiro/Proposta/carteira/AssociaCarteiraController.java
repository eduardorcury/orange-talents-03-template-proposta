package com.zup.academy.eduardoribeiro.Proposta.carteira;

import com.zup.academy.eduardoribeiro.Proposta.cartao.Cartao;
import com.zup.academy.eduardoribeiro.Proposta.cartao.CartaoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.Optional;

@RestController
public class AssociaCarteiraController {

    private final CartaoRepository cartaoRepository;
    private final CarteiraRepository carteiraRepository;
    private final Logger LOGGER = LoggerFactory.getLogger("jsonLogger");

    public AssociaCarteiraController(CartaoRepository cartaoRepository,
                                     CarteiraRepository carteiraRepository) {
        this.cartaoRepository = cartaoRepository;
        this.carteiraRepository = carteiraRepository;
    }

    @PostMapping(value = "/cartoes/{id}/carteiras")
    public ResponseEntity<?> associa(@PathVariable("id") Long cartaoId,
                                     @RequestBody @Valid AssociacaoCarteiraRequest request,
                                     UriComponentsBuilder uriBuilder) {

        Optional<Cartao> possivelCartao = cartaoRepository.findById(cartaoId);

        if (possivelCartao.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Cartao cartao = possivelCartao.get();

        if (carteiraRepository.existsByCartao(cartao)) {
            LOGGER.error("Tentativa de associar carteira já existente ao cartão de id {}", cartaoId);
            return ResponseEntity.unprocessableEntity().build();
        }

        Carteira carteira = request.toModel(cartao);
        carteiraRepository.save(carteira);
        URI uri = uriBuilder.path("/cartoes/{cartaoId}/carteiras/{carteiraId}")
                .buildAndExpand(cartaoId, carteira.getId())
                .toUri();
        LOGGER.info("Carteira de id {} associada ao cartão de id {}", carteira.getId(), cartaoId);
        return ResponseEntity.created(uri).build();

    }

}
