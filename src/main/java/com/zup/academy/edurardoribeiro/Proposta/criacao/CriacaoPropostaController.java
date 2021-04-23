package com.zup.academy.edurardoribeiro.Proposta.criacao;

import com.zup.academy.edurardoribeiro.Proposta.analise.AnaliseFinanceiraService;
import com.zup.academy.edurardoribeiro.Proposta.compartilhado.erros.ErroPadronizado;
import com.zup.academy.edurardoribeiro.Proposta.analise.PedidoAnaliseFinanceira;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.io.IOException;
import java.net.URI;
import java.util.Set;

@RestController
@RequestMapping("/propostas")
public class CriacaoPropostaController {

    private final PropostaRepository propostaRepository;
    private final AnaliseFinanceiraService analiseService;
    private final Logger logger = LoggerFactory.getLogger("jsonLogger");

    public CriacaoPropostaController(PropostaRepository propostaRepository,
                                     AnaliseFinanceiraService analiseService) {
        this.propostaRepository = propostaRepository;
        this.analiseService = analiseService;
    }


    @PostMapping
    public ResponseEntity<?> cria(@RequestBody @Valid NovaPropostaRequest request,
                               UriComponentsBuilder uriComponentsBuilder) throws IOException {

        if (propostaRepository.existsByDocumento(request.getDocumento())) {
            return ResponseEntity.status(422).body(new ErroPadronizado(Set.of("Erro de criação de proposta")));
        }

        Proposta proposta = request.toModel();

        propostaRepository.save(proposta);
        URI uri = uriComponentsBuilder
                .path("/propostas/{id}")
                .buildAndExpand(proposta.getId())
                .toUri();

        logger.info("Proposta de documento {}, nome {} e salário {} criada. URI é {}",
                proposta.retornaDocumentoOfuscado(), proposta.getNome(), proposta.getSalario(), uri.toString());

        analiseService.analise(proposta);

        return ResponseEntity.created(uri).build();

    }

}
