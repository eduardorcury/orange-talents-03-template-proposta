package com.zup.academy.eduardoribeiro.Proposta.compartilhado.validacao;

import org.hibernate.validator.constraints.CompositionType;
import org.hibernate.validator.constraints.ConstraintComposition;
import org.hibernate.validator.constraints.br.CNPJ;
import org.hibernate.validator.constraints.br.CPF;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * A String anotada deve ser um CPF ou um CNPJ válido.
 *
 * Elementos nulos são considerados válidos.
 *
 * @author Eduardo Ribeiro Cury
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@ConstraintComposition(CompositionType.OR)
@CPF
@CNPJ
@Constraint(validatedBy = { })
@Documented
public @interface CpfOuCnpj {

    String message() default "Formato de documento inválido. Documentos válidos são CPF ou CNPJ";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
