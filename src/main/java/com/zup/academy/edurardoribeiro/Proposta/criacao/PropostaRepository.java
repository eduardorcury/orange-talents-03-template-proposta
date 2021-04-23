package com.zup.academy.edurardoribeiro.Proposta.criacao;

import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.CrudRepository;

import javax.persistence.QueryHint;

public interface PropostaRepository extends CrudRepository<Proposta, Long> {

    @QueryHints(value = {
            @QueryHint(name = "javax.persistence.query.timeout", value = "100")
    })
    Boolean existsByDocumento(String documento);

}
