package com.zup.academy.edurardoribeiro.Proposta.analise;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "analise-financeira", url = "${sistemas.analise-financeira.url}")
public interface AnaliseFinanceiraClient {

    @RequestMapping(method = RequestMethod.POST, value = "/api/solicitacao")
    RespostaAnaliseFinanceira consulta(@RequestBody PedidoAnaliseFinanceira pedido);

    @RequestMapping(method = RequestMethod.GET, value = "/actuator/health")
    String healthCheck();

}
