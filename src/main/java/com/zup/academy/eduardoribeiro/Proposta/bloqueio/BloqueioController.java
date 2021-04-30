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
    private final BloqueadorDeCartoes bloqueador;
    private final Logger logger = LoggerFactory.getLogger("jsonLogger");

    public BloqueioController(CartaoRepository cartaoRepository,
                              BloqueadorDeCartoes bloqueador) {
        this.cartaoRepository = cartaoRepository;
        this.bloqueador = bloqueador;
    }

    @PostMapping("/cartoes/{id}/bloqueio")
    public ResponseEntity<?> bloquearCartao(@PathVariable("id") Long cartaoId,
                                            @RequestHeader(value = "User-Agent", required = true) String userAgent,
                                            @RequestBody @Valid NovoBloqueioRequest request) {

        Optional<Cartao> cartaoOptional = cartaoRepository.findById(cartaoId);

        if (cartaoOptional.isPresent()) {
            bloqueador.bloqueiaCartao(cartaoOptional.get(), userAgent, request);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }

    }

}
