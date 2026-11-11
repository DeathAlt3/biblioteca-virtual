package br.edu.christus.backend.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Tratamento centralizado de erros: qualquer exceção lançada nos controllers/services
 * que chegue aqui é convertida em um JSON padronizado (ErroResponse) com o código HTTP adequado.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // 400 - Falhou alguma validação do DTO (@NotBlank, @Min, @Size...)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErroResponse> handleValidacao(MethodArgumentNotValidException ex, HttpServletRequest request) {
        Map<String, String> campos = new LinkedHashMap<>();
        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            campos.putIfAbsent(fieldError.getField(), fieldError.getDefaultMessage());
        }
        return montar(HttpStatus.BAD_REQUEST, "Um ou mais campos estão inválidos. Verifique o objeto 'campos'.",
                request, campos);
    }

    // Erros lançados pelos services: 404 (não encontrado), 409 (conflito), 400 (regra de negócio)...
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ErroResponse> handleResponseStatus(ResponseStatusException ex, HttpServletRequest request) {
        return montar(ex.getStatusCode(), ex.getReason(), request, null);
    }

    // 400 - JSON mal formatado, enum com valor inexistente, data em formato errado...
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErroResponse> handleJsonInvalido(HttpMessageNotReadableException ex, HttpServletRequest request) {
        return montar(HttpStatus.BAD_REQUEST,
                "Corpo da requisição inválido. Verifique o formato do JSON, as datas (aaaa-mm-dd) "
                        + "e os valores permitidos para campos como tipo, formato e areaConhecimento.",
                request, null);
    }

    // 400 - Parâmetro da URL com tipo errado, ex: /api/v1/livros/abc
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErroResponse> handleTipoParametro(MethodArgumentTypeMismatchException ex, HttpServletRequest request) {
        return montar(HttpStatus.BAD_REQUEST,
                "Valor inválido para o parâmetro '" + ex.getName() + "': " + ex.getValue(),
                request, null);
    }

    // 409 - Restrição do banco violada (ex: e-mail ou ISBN duplicado, chave estrangeira)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErroResponse> handleIntegridade(DataIntegrityViolationException ex, HttpServletRequest request) {
        return montar(HttpStatus.CONFLICT,
                "A operação viola uma restrição do banco de dados (valor duplicado ou registro vinculado a outro).",
                request, null);
    }

    // 405 - Método HTTP não suportado na rota
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErroResponse> handleMetodo(HttpRequestMethodNotSupportedException ex, HttpServletRequest request) {
        return montar(HttpStatus.METHOD_NOT_ALLOWED,
                "O método " + ex.getMethod() + " não é suportado nesta rota.", request, null);
    }

    // 404 - Rota inexistente
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ErroResponse> handleRotaInexistente(NoResourceFoundException ex, HttpServletRequest request) {
        return montar(HttpStatus.NOT_FOUND, "Rota não encontrada.", request, null);
    }

    // 500 - Qualquer erro inesperado (o detalhe vai para o log, não para o cliente)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErroResponse> handleGenerico(Exception ex, HttpServletRequest request) {
        log.error("Erro inesperado em {}", request.getRequestURI(), ex);
        return montar(HttpStatus.INTERNAL_SERVER_ERROR,
                "Erro interno inesperado. Tente novamente mais tarde.", request, null);
    }

    private ResponseEntity<ErroResponse> montar(HttpStatusCode status, String mensagem,
                                                HttpServletRequest request, Map<String, String> campos) {
        HttpStatus httpStatus = HttpStatus.resolve(status.value());
        String erro = httpStatus != null ? httpStatus.getReasonPhrase() : String.valueOf(status.value());
        ErroResponse body = new ErroResponse(LocalDateTime.now(), status.value(), erro, mensagem,
                request.getRequestURI(), campos);
        return ResponseEntity.status(status).body(body);
    }
}
