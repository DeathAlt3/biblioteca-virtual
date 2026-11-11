package br.edu.christus.backend.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Dados recebidos para cadastrar ou editar um Autor.
 * As anotações de validação são verificadas quando o controller usa @Valid.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AutorRequestDTO {

    @NotBlank(message = "O nome do autor é obrigatório")
    @Size(max = 150, message = "O nome do autor deve ter no máximo 150 caracteres")
    private String nome;

    @NotBlank(message = "A nacionalidade é obrigatória")
    @Size(max = 60, message = "A nacionalidade deve ter no máximo 60 caracteres")
    private String nacionalidade;

    @Past(message = "A data de nascimento deve estar no passado")
    private LocalDate dataNascimento;

    @Size(max = 1000, message = "A biografia deve ter no máximo 1000 caracteres")
    private String biografia;
}
