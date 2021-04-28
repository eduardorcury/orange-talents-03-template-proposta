package com.zup.academy.edurardoribeiro.Proposta.biometria;

import com.zup.academy.edurardoribeiro.Proposta.cartao.Cartao;
import com.zup.academy.edurardoribeiro.Proposta.cartao.CartaoRepository;
import com.zup.academy.edurardoribeiro.Proposta.compartilhado.validacao.Base64Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.Optional;

@RestController
public class CadastroBiometriaController {

    private final CartaoRepository cartaoRepository;
    private final BiometriaRepository biometriaRepository;
    private final Base64Validator base64Validator;
    private final Logger logger = LoggerFactory.getLogger("jsonLogger");

    public CadastroBiometriaController(CartaoRepository cartaoRepository,
                                       BiometriaRepository biometriaRepository,
                                       Base64Validator base64Validator) {
        this.cartaoRepository = cartaoRepository;
        this.biometriaRepository = biometriaRepository;
        this.base64Validator = base64Validator;
    }

    @InitBinder
    private void initBinder(WebDataBinder dataBinder) {
        dataBinder.addValidators(base64Validator);
    }

    @PostMapping("/cartoes/{id}/biometrias")
    public ResponseEntity<?> cadastraBiometria(@PathVariable("id") String cartaoId,
                                               @RequestBody @Valid CadastroBiometriaRequest request,
                                               UriComponentsBuilder uriComponentsBuilder) {

        Optional<Cartao> cartaoOptional = cartaoRepository.findByIdExterno(cartaoId);

        if (cartaoOptional.isPresent()) {
            Biometria biometria = request.toModel(cartaoOptional.get());
            biometriaRepository.save(biometria);
            URI uri = uriComponentsBuilder
                    .path("/cartoes/{cartaoId}/biometrias/{biometriaId}")
                    .buildAndExpand(cartaoId, biometria.getId())
                    .toUri();
            logger.info("Biometria do cartão {} criada. URI é {}",
                    cartaoOptional.get().retornaCartaoOfuscado(), uri.toString());
            return ResponseEntity.created(uri).build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
