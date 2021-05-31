package com.zup.academy.eduardoribeiro.Proposta.carteira;

import com.zup.academy.eduardoribeiro.Proposta.cartao.Cartao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CarteiraRepository extends JpaRepository<Carteira, String> {

    Boolean existsByCartaoAndTipo(Cartao cartao, TipoDeCarteira tipo);

}
