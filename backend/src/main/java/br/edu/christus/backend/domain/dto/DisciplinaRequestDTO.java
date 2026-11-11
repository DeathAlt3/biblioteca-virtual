package br.edu.christus.backend.domain.dto;

import br.edu.christus.backend.domain.model.AreaConhecimento;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DisciplinaRequestDTO {

    @NotBlank(message = "O nome da disciplina é obrigatório")
    @Size(max = 100, message = "O nome da disciplina deve ter no máximo 100 caracteres")
    private String nome;

    @NotNull(message = "A área do conhecimento é obrigatória (LINGUAGENS, MATEMATICA, CIENCIAS_DA_NATUREZA ou CIENCIAS_HUMANAS)")
    private AreaConhecimento areaConhecimento;

    @NotNull(message = "A série é obrigatória")
    @Min(value = 1, message = "A série deve ser 1, 2 ou 3")
    @Max(value = 3, message = "A série deve ser 1, 2 ou 3")
    private Integer serie;

    @Size(max = 500, message = "A descrição deve ter no máximo 500 caracteres")
    private String descricao;
}
