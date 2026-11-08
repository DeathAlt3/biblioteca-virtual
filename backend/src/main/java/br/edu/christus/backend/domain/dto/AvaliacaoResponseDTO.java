package br.edu.christus.backend.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AvaliacaoResponseDTO {
    private Long id;
    private Integer nota;
    private String comentario;
    private LocalDate dataAvaliacao;
    private Boolean recomendariaColegas;
    private UserLowDTO usuario;
    private LivroResponseDTO livro;
}
