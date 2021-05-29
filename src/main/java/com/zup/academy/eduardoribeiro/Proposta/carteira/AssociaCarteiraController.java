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
import java.util.Optional;

@RestController
public class AssociaCarteiraController {

    private final CartaoRepository cartaoRepository;
    private final AssociadorDeCarteiras associador;
    private final Logger LOGGER = LoggerFactory.getLogger("jsonLogger");

    public AssociaCarteiraController(CartaoRepository cartaoRepository,
                                     AssociadorDeCarteiras associador) {
        this.cartaoRepository = cartaoRepository;
        this.associador = associador;
    }

    @PostMapping(value = "/cartoes/{id}/carteiras")
    public ResponseEntity<?> associa(@PathVariable("id") Long cartaoId,
                                     @RequestBody @Valid AssociacaoCarteiraRequest request,
                                     UriComponentsBuilder uriBuilder) {

        Optional<Cartao> possivelCartao = cartaoRepository.findById(cartaoId);

        if (possivelCartao.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return associador.associaCarteira(possivelCartao.get(), request, uriBuilder);

    }

}
