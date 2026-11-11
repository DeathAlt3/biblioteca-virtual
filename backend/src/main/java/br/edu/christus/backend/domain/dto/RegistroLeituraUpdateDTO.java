package br.edu.christus.backend.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Edição dos dados "manuais" de um registro de leitura.
 * O status CONCLUIDO não pode ser escolhido aqui: ele é calculado pelo progresso (PATCH /progresso).
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegistroLeituraUpdateDTO {

    @NotBlank(message = "O status é obrigatório")
    @Pattern(regexp = "EM_ANDAMENTO|PAUSADO",
            message = "O status deve ser EM_ANDAMENTO ou PAUSADO (CONCLUIDO é definido automaticamente pelo progresso)")
    private String status;

    @Size(max = 30, message = "O nome do dispositivo deve ter no máximo 30 caracteres (ex: SMARTPHONE, COMPUTADOR)")
    private String dispositivo;
}
