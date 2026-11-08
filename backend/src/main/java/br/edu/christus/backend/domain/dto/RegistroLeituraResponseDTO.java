package br.edu.christus.backend.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegistroLeituraResponseDTO {
    private Long id;
    private LocalDate dataInicio;
    private LocalDate dataUltimoAcesso;
    private Integer paginasLidas;
    private Double percentualConcluido;
    private Integer tempoEstudoMinutos;
    private String status;
    private String dispositivo;
    private Integer estimativaDiasParaConcluir;
    private UserLowDTO usuario;
    private LivroResponseDTO livro;
}
