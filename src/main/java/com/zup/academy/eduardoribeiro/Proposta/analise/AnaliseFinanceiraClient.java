package com.zup.academy.eduardoribeiro.Proposta.analise;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@FeignClient(name = "analise-financeira", url = "${sistemas.analise-financeira.url}")
public interface AnaliseFinanceiraClient {

    @RequestMapping(method = POST, value = "/api/solicitacao")
    RespostaAnaliseFinanceira consulta(@RequestBody PedidoAnaliseFinanceira pedido);

    @RequestMapping(method = GET, value = "/actuator/health")
    void healthCheck();

}
