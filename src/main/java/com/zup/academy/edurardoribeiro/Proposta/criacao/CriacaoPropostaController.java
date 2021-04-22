package com.zup.academy.edurardoribeiro.Proposta.criacao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/propostas")
public class CriacaoPropostaController {

    private final EntityManager entityManager;
    private final Logger logger = LoggerFactory.getLogger(CriacaoPropostaController.class);

    public CriacaoPropostaController(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @PostMapping
    @Transactional
    public ResponseEntity<?> cria(@RequestBody @Valid NovaPropostaRequest request,
                               UriComponentsBuilder uriComponentsBuilder) {

        Proposta proposta = request.toModel();
        entityManager.persist(proposta);
        URI uri = uriComponentsBuilder
                .path("/propostas/{id}")
                .buildAndExpand(proposta.getId())
                .toUri();

        logger.info("Proposta de documento {}, nome {} e salário {} criada. URI é {}",
                proposta.retornaDocumentoOfuscado(), proposta.getNome(), proposta.getSalario(), uri.toString());

        return ResponseEntity.created(uri).build();

    }

}
