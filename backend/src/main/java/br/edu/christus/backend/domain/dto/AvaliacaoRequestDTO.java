package br.edu.christus.backend.domain.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AvaliacaoRequestDTO {

    @NotNull(message = "O ID do usuário/aluno é obrigatório")
    private Long usuarioId;

    @NotNull(message = "O ID do livro é obrigatório")
    private Long livroId;

    @NotNull(message = "A nota é obrigatória")
    @Min(value = 1, message = "A nota mínima é 1")
    @Max(value = 5, message = "A nota máxima é 5")
    private Integer nota;

    @Size(max = 500, message = "O comentário deve ter no máximo 500 caracteres")
    private String comentario;

    @NotNull(message = "Informe se recomendaria o livro aos colegas")
    private Boolean recomendariaColegas;
}
