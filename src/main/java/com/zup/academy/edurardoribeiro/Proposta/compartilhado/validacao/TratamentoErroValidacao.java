package com.zup.academy.edurardoribeiro.Proposta.compartilhado.validacao;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class TratamentoErroValidacao {

    private final MessageSource messageSource;

    public TratamentoErroValidacao(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ExceptionHandler
    public ResponseEntity<List<ExceptionResponse>> trataErroValidacao(MethodArgumentNotValidException exception) {

        List<ExceptionResponse> erros = exception
                .getFieldErrors()
                .stream()
                .map(erro -> new ExceptionResponse(
                                erro.getField(),
                                messageSource.getMessage(erro, LocaleContextHolder.getLocale())
                ))
                .collect(Collectors.toList());

        return ResponseEntity.badRequest().body(erros);

    }
}
