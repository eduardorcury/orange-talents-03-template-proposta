package com.zup.academy.eduardoribeiro.Proposta.compartilhado.validacao;

import com.google.common.net.InetAddresses;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class IpAddressValidator implements ConstraintValidator<IpAddress, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {

        if (value != null) {
            return InetAddresses.isInetAddress(value);
        }
        return true;

    }
}
