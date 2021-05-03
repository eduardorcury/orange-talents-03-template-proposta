package com.zup.academy.eduardoribeiro.Proposta.viagem;

import com.zup.academy.eduardoribeiro.Proposta.cartao.Cartao;
import com.zup.academy.eduardoribeiro.Proposta.cartao.CartaoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@RestController
public class AvisoViagemController {

    private final CartaoRepository cartaoRepository;
    private final AvisoViagemRepository avisoRepository;
    private final Logger LOGGER = LoggerFactory.getLogger("jsonLogger");

    public AvisoViagemController(CartaoRepository cartaoRepository,
                                 AvisoViagemRepository avisoRepository) {
        this.cartaoRepository = cartaoRepository;
        this.avisoRepository = avisoRepository;
    }

    @PostMapping("/cartoes/{id}/avisos")
    public ResponseEntity<?> criaAvisoViagem(@PathVariable("id") Long cartaoId,
                                             @RequestHeader(value = "User-Agent", required = true) String userAgent,
                                             @RequestBody @Valid NovoAvisoRequest request) {

        Optional<Cartao> cartaoOptional = cartaoRepository.findById(cartaoId);

        if (cartaoOptional.isPresent()) {
            AvisoViagem aviso = request.toModel(cartaoOptional.get(), userAgent);
            avisoRepository.save(aviso);
            LOGGER.info("Aviso de Viagem criado para o cartão de ID {}", cartaoId);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }

    }
}
