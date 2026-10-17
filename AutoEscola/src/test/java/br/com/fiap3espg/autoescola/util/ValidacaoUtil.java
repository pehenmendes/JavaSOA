package br.com.fiap3espg.autoescola.util;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;

import java.util.Set;
import java.util.stream.Collectors;

/** Executa o Bean Validation sem subir o contexto do Spring. */
public final class ValidacaoUtil {
    private static final Validator VALIDATOR = Validation.buildDefaultValidatorFactory().getValidator();

    private ValidacaoUtil() {
    }

    /** Retorna os caminhos das propriedades inválidas (ex.: "cpf", "endereco.uf"). */
    public static Set<String> camposInvalidos(Object dto) {
        return VALIDATOR.validate(dto).stream()
                .map(ConstraintViolation::getPropertyPath)
                .map(Object::toString)
                .collect(Collectors.toSet());
    }
}
