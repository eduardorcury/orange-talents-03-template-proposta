package com.zup.academy.edurardoribeiro.Proposta.compartilhado.erros;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Set;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ErroHandlerAdvice {

    private final MessageSource messageSource;
    private final ObjectMapper mapper;

    public ErroHandlerAdvice(MessageSource messageSource, ObjectMapper mapper) {
        this.messageSource = messageSource;
        this.mapper = mapper;
    }

    @ExceptionHandler
    public ResponseEntity<ErroPadronizado> trataErroValidacao(MethodArgumentNotValidException exception) {

        Set<String> mensagens = exception
                .getFieldErrors()
                .stream()
                .map(erro -> String.format("Campo %s %s",
                        erro.getField(),
                        messageSource.getMessage(erro, LocaleContextHolder.getLocale())
                ))
                .collect(Collectors.toSet());

        ErroPadronizado erro = new ErroPadronizado(mensagens);

        return ResponseEntity.badRequest().body(erro);

    }
}
