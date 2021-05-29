package com.zup.academy.eduardoribeiro.Proposta.cartao;

import com.zup.academy.eduardoribeiro.Proposta.bloqueio.NotificacaoBloqueio;
import com.zup.academy.eduardoribeiro.Proposta.carteira.NotificacaoCarteira;
import com.zup.academy.eduardoribeiro.Proposta.viagem.NotificacaoViagem;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@FeignClient(name = "cartoes", url = "${sistemas.cartoes.url}")
public interface CartaoClient {

    @RequestMapping(method = GET, value = "/api/cartoes")
    ConsultaCartaoResponse consultaCartao(@RequestParam(name = "idProposta", required = true) Long idProposta);

    @RequestMapping(method = POST, value = "/api/cartoes/{id}/bloqueios")
    String notificaBloqueio(@PathVariable("id") String cartaoId, @RequestBody NotificacaoBloqueio notificacao);

    @RequestMapping(method = POST, value = "/api/cartoes/{id}/avisos")
    String notificaViagem(@PathVariable("id") String cartaoId, @RequestBody NotificacaoViagem notificacao);

    @RequestMapping(method = POST, value = "/api/cartoes/{id}/carteiras")
    String associaCarteira(@PathVariable("id") String cartaoId, @RequestBody NotificacaoCarteira notificacao);

    @RequestMapping(method = GET, value = "/actuator/health")
    void healthCheck();

}
