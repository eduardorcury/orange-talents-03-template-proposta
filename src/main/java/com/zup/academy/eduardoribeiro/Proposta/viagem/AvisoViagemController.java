package com.zup.academy.eduardoribeiro.Proposta.viagem;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class AvisoViagemController {

    private final NotificadorDeViagens notificador;
    private final Logger LOGGER = LoggerFactory.getLogger("jsonLogger");

    public AvisoViagemController(NotificadorDeViagens notificador) {
        this.notificador = notificador;
    }

    @PostMapping("/cartoes/{id}/avisos")
    public ResponseEntity<?> criaAvisoViagem(@PathVariable("id") Long cartaoId,
                                             @RequestHeader(value = "User-Agent", required = true) String userAgent,
                                             @RequestBody @Valid NovoAvisoRequest request) {

        return notificador.notifica(cartaoId, request, userAgent);

    }
}
