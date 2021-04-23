package com.zup.academy.edurardoribeiro.Proposta.analise;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "analise-financeira", url = "${analise-financeira.url}")
public interface AnaliseFinanceiraClient {

    @RequestMapping(method = RequestMethod.POST)
    RespostaAnaliseFinanceira consulta(@RequestBody PedidoAnaliseFinanceira pedido);

}
