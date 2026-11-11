package br.edu.christus.backend.domain.dto;

import br.edu.christus.backend.domain.model.AreaConhecimento;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DisciplinaResponseDTO {
    private Long id;
    private String nome;
    private AreaConhecimento areaConhecimento;
    private Integer serie;
    private String descricao;
}
