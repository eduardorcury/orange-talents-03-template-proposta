package com.zup.academy.edurardoribeiro.Proposta.cartao;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartaoRepository extends JpaRepository<Cartao, Long> {

    Optional<Cartao> findByIdExterno(String id);

}
