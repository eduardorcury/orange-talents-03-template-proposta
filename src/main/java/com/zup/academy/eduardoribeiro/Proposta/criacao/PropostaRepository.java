package com.zup.academy.eduardoribeiro.Proposta.criacao;

import org.springframework.cache.annotation.Cacheable;
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
    @Cacheable(cacheNames = "propostasElegiveis", condition = "#status.toString().equals('ELEGIVEL')")
    List<Proposta> findByStatus(StatusProposta status);

}
