package br.edu.christus.backend.exception;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Formato padrão de todas as respostas de erro da API.
 * "campos" só é preenchido em erros de validação (campo -> mensagem).
 */
public record ErroResponse(
        LocalDateTime timestamp,
        int status,
        String erro,
        String mensagem,
        String caminho,
        Map<String, String> campos
) {
}
