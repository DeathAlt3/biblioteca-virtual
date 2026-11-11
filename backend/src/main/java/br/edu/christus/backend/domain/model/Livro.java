package br.edu.christus.backend.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

/**
 * Livro ou material digital do acervo (livro, apostila, artigo ou resumo).
 * É a entidade central: Avaliacao e RegistroLeitura apontam para ela.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tb_livro")
public class Livro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String titulo;

    @Column(unique = true, length = 20)
    private String isbn; // opcional: apostilas e resumos normalmente não têm ISBN

    @Column(name = "ano_publicacao", nullable = false)
    private Integer anoPublicacao;

    @Column(name = "numero_paginas", nullable = false)
    private Integer numeroPaginas;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TipoMaterial tipo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private FormatoDigital formato;

    @Column(name = "url_acesso", nullable = false, length = 500)
    private String urlAcesso;

    @Column(length = 2000)
    private String sinopse;

    @Column(name = "total_acessos", nullable = false)
    private Integer totalAcessos = 0;

    @Column(name = "data_cadastro", nullable = false)
    private LocalDate dataCadastro;

    // Muitos livros para um autor -> coluna autor_id (chave estrangeira) em tb_livro
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "autor_id", nullable = false)
    private Autor autor;

    // Muitos livros para uma disciplina -> coluna disciplina_id em tb_livro
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "disciplina_id", nullable = false)
    private Disciplina disciplina;
}
