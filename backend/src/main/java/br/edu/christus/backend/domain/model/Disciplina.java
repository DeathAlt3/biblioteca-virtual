package br.edu.christus.backend.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Disciplina escolar usada para organizar o acervo (ex: Biologia - 1º ano).
 * Relacionamento: uma Disciplina pode ter vários Livros (o lado @ManyToOne fica em Livro).
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tb_disciplina")
public class Disciplina {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String nome;

    @Enumerated(EnumType.STRING)
    @Column(name = "area_conhecimento", nullable = false, length = 30)
    private AreaConhecimento areaConhecimento;

    @Column(nullable = false)
    private Integer serie; // 1, 2 ou 3 (ano do ensino médio)

    @Column(length = 500)
    private String descricao;
}
