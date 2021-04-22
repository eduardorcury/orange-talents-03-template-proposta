package com.zup.academy.edurardoribeiro.Proposta.criacao;

import org.springframework.data.repository.CrudRepository;

public interface PropostaRepository extends CrudRepository<Proposta, Long> {

    Boolean existsByDocumento(String documento);

}
