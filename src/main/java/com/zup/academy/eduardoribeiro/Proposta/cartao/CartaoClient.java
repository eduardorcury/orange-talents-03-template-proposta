package com.zup.academy.eduardoribeiro.Proposta.cartao;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@FeignClient(name = "cartoes", url = "${sistemas.cartoes.url}")
public interface CartaoClient {

    @RequestMapping(method = GET, value = "/api/cartoes")
    ConsultaCartaoResponse consultaCartao(@RequestParam(name = "idProposta", required = true) Long idProposta);

    @RequestMapping(method = GET, value = "/actuator/health")
    void healthCheck();

}
