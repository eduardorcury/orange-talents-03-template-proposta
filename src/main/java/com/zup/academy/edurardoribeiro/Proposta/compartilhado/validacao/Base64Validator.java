package com.zup.academy.edurardoribeiro.Proposta.compartilhado.validacao;

import com.zup.academy.edurardoribeiro.Proposta.biometria.CadastroBiometriaRequest;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Base64;

@Component
public class Base64Validator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(CadastroBiometriaRequest.class);
    }

    @Override
    public void validate(Object target, Errors errors) {

        if (errors.hasErrors()) {
            return;
        }

        CadastroBiometriaRequest request = (CadastroBiometriaRequest) target;
        String fingerprint = request.getFingerprint();

        try {
            Base64.getDecoder().decode(fingerprint);
        } catch (IllegalArgumentException exception) {
            errors.rejectValue("fingerprint", null, "Biometria deve estar em formato Base64");
        }

    }
}
