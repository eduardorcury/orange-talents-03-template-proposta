package com.zup.academy.eduardoribeiro.Proposta.compartilhado.validacao;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * A String anotada deve ser um endereço IP válido.
 *
 * Elementos nulos são considerados válidos.
 *
 * @author Eduardo Ribeiro Cury
 */
@Documented
@Constraint(validatedBy = IpAddressValidator.class)
@Target({ FIELD })
@Retention(RUNTIME)
public @interface IpAddress {

    String message() default "Endereço IP inválido";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};


}
