package com.zup.academy.edurardoribeiro.Proposta.criacao;

import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.CrudRepository;

import javax.persistence.QueryHint;
import java.util.List;

public interface PropostaRepository extends CrudRepository<Proposta, Long> {

    @QueryHints(value = {
            @QueryHint(name = "javax.persistence.query.timeout", value = "100")
    })
    Boolean existsByDocumento(String documento);

    @QueryHints(value = {
            @QueryHint(name = "javax.persistence.query.timeout", value = "300")
    })
    List<Proposta> findByStatus(String status);

}
