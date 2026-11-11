package br.edu.christus.backend.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/** Dados de Autor devolvidos pela API. */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AutorResponseDTO {
    private Long id;
    private String nome;
    private String nacionalidade;
    private LocalDate dataNascimento;
    private String biografia;
}
