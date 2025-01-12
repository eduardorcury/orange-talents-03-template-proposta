package com.zup.academy.eduardoribeiro.Proposta;

import org.hibernate.validator.internal.constraintvalidators.bv.EmailValidator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.openfeign.EnableFeignClients;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.validation.ConstraintValidator;

@SpringBootApplication
@EnableFeignClients
@EnableCaching
@EnableSwagger2
public class PropostaApplication {

	public static void main(String[] args) {
		SpringApplication.run(PropostaApplication.class, args);
	}

}
