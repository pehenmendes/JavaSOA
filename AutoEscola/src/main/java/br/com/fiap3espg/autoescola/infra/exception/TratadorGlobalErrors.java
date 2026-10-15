package br.com.fiap3espg.autoescola.infra.exception;

import br.com.fiap3espg.autoescola.domain.aluno.AlunoNotFoundException;
import br.com.fiap3espg.autoescola.domain.instrucao.InstrucaoNotFoundException;
import br.com.fiap3espg.autoescola.domain.instrucao.ValidacaoException;
import br.com.fiap3espg.autoescola.domain.instrutor.InstrutorNotFoundException;
import br.com.fiap3espg.autoescola.domain.usuario.SenhaAtualIncorretaException;
import br.com.fiap3espg.autoescola.domain.usuario.SenhaIguaisException;
import br.com.fiap3espg.autoescola.domain.usuario.UsuarioNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class TratadorGlobalErrors {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Void> tratarNotFound() {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler({
            InstrutorNotFoundException.class,
            AlunoNotFoundException.class,
            UsuarioNotFoundException.class,
            InstrucaoNotFoundException.class
    })
    public ResponseEntity<DadosMessage> tratarNaoEncontrado(RuntimeException ex) {
        return resposta(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<DadosBadRequest>> tratarBadRequest(MethodArgumentNotValidException ex) {
        List<DadosBadRequest> erros = ex.getFieldErrors()
                .stream()
                .map(DadosBadRequest::new)
                .toList();
        return ResponseEntity.badRequest().body(erros);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<DadosMessage> tratarCorpoInvalido() {
        return resposta(HttpStatus.BAD_REQUEST, "Corpo da requisição ausente ou inválido");
    }

    @ExceptionHandler({
            ValidacaoException.class,
            SenhaIguaisException.class,
            SenhaAtualIncorretaException.class
    })
    public ResponseEntity<DadosMessage> tratarRegraDeNegocio(RuntimeException ex) {
        return resposta(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler({BadCredentialsException.class, DisabledException.class})
    public ResponseEntity<DadosMessage> tratarCredenciais() {
        return resposta(HttpStatus.UNAUTHORIZED, "Login ou senha inválidos");
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<DadosMessage> tratarAcessoNegado(AccessDeniedException ex) {
        return resposta(HttpStatus.FORBIDDEN, "Acesso negado");
    }

    private ResponseEntity<DadosMessage> resposta(HttpStatus status, String mensagem) {
        return ResponseEntity.status(status).body(new DadosMessage(mensagem));
    }

    private record DadosBadRequest(String field, String message) {
        public DadosBadRequest(FieldError erro) {
            this(erro.getField(), erro.getDefaultMessage());
        }
    }

    private record DadosMessage(String message) {
    }
}
