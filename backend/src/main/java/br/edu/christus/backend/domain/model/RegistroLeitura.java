package br.edu.christus.backend.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tb_registro_leitura")
public class RegistroLeitura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "data_inicio", nullable = false)
    private LocalDate dataInicio;

    @Column(name = "data_ultimo_acesso", nullable = false)
    private LocalDate dataUltimoAcesso;

    @Column(name = "paginas_lidas", nullable = false)
    private Integer paginasLidas = 0;

    @Column(name = "percentual_concluido", nullable = false)
    private Double percentualConcluido = 0.0;

    @Column(name = "tempo_estudo_minutos", nullable = false)
    private Integer tempoEstudoMinutos = 0;

    @Column(nullable = false, length = 20)
    private String status; // "EM_ANDAMENTO", "CONCLUIDO", "PAUSADO"

    @Column(length = 30)
    private String dispositivo; // "SMARTPHONE", "TABLET", "COMPUTADOR"

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private User usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "livro_id", nullable = false)
    private Livro livro;
}
