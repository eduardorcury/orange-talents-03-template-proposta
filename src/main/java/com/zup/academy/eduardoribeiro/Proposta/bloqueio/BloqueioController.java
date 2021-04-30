package com.zup.academy.eduardoribeiro.Proposta.bloqueio;

import com.zup.academy.eduardoribeiro.Proposta.cartao.Cartao;
import com.zup.academy.eduardoribeiro.Proposta.cartao.CartaoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@RestController
public class BloqueioController {

    private final CartaoRepository cartaoRepository;
    private final BloqueioRepository bloqueioRepository;
    private final Logger logger = LoggerFactory.getLogger("jsonLogger");

    public BloqueioController(CartaoRepository cartaoRepository,
                              BloqueioRepository bloqueioRepository) {
        this.cartaoRepository = cartaoRepository;
        this.bloqueioRepository = bloqueioRepository;
    }

    @PostMapping("/cartoes/{id}/bloqueio")
    public ResponseEntity<?> bloquearCartao(@PathVariable("id") Long cartaoId,
                                            @RequestHeader(value = "User-Agent", required = true) String userAgent,
                                            @RequestBody @Valid NovoBloqueioRequest request) {

        Optional<Cartao> cartaoOptional = cartaoRepository.findById(cartaoId);

        return cartaoOptional.map(cartao -> {
            if (cartao.bloqueado()) {
                return ResponseEntity.unprocessableEntity().build();
            } else {
                Bloqueio bloqueio = request.toModel(userAgent, cartao);
                bloqueioRepository.save(bloqueio);
                cartao.bloquearCartao();
                cartaoRepository.save(cartao);
                logger.info("Bloqueio criado para o cartão de ID {}", cartaoId);
                return ResponseEntity.ok().build();
            }
        }).orElseGet(() -> ResponseEntity.notFound().build());

    }
}
