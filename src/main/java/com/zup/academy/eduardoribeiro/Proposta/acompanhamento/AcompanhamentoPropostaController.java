package com.zup.academy.eduardoribeiro.Proposta.acompanhamento;

import com.zup.academy.eduardoribeiro.Proposta.criacao.Proposta;
import com.zup.academy.eduardoribeiro.Proposta.criacao.PropostaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class AcompanhamentoPropostaController {

    private final PropostaRepository propostaRepository;

    public AcompanhamentoPropostaController(PropostaRepository propostaRepository) {
        this.propostaRepository = propostaRepository;
    }

    @GetMapping("/propostas/{id:^[0-9]*$}")
    public ResponseEntity<ConsultaPropostaResponse> mostraDetalhes(@PathVariable("id") Long propostaId) {

        Optional<Proposta> propostaOptional = propostaRepository.findById(propostaId);

        return propostaOptional
                .map(ConsultaPropostaResponse::new)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
