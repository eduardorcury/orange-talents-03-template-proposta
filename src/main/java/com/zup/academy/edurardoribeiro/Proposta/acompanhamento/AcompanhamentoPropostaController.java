package com.zup.academy.edurardoribeiro.Proposta.acompanhamento;

import com.zup.academy.edurardoribeiro.Proposta.criacao.Proposta;
import com.zup.academy.edurardoribeiro.Proposta.criacao.PropostaRepository;
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
    public ResponseEntity<DetalhesPropostaResponse> mostraDetalhes(@PathVariable("id") Long propostaId) {

        Optional<Proposta> propostaOptional = propostaRepository.findById(propostaId);

        if (propostaOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            DetalhesPropostaResponse response = new DetalhesPropostaResponse(propostaOptional.get());
            return ResponseEntity.ok(response);
        }

    }
}
