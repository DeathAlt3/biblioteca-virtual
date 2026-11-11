package br.edu.christus.backend.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

/**
 * Autor de um livro ou material digital do acervo.
 * Relacionamento: um Autor pode ter vários Livros (o lado @ManyToOne fica em Livro).
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tb_autor")
public class Autor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String nome;

    @Column(nullable = false, length = 60)
    private String nacionalidade;

    @Column(name = "data_nascimento")
    private LocalDate dataNascimento;

    @Column(length = 1000)
    private String biografia;
}
