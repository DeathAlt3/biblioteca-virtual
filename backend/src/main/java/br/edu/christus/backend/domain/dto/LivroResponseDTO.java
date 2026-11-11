package br.edu.christus.backend.domain.dto;

import br.edu.christus.backend.domain.model.FormatoDigital;
import br.edu.christus.backend.domain.model.Livro;
import br.edu.christus.backend.domain.model.TipoMaterial;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Dados de Livro devolvidos pela API.
 * Em vez de devolver os objetos Autor e Disciplina inteiros, devolve só o ID e o nome
 * (resposta menor e sem risco de referência circular).
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LivroResponseDTO {
    private Long id;
    private String titulo;
    private String isbn;
    private Integer anoPublicacao;
    private Integer numeroPaginas;
    private TipoMaterial tipo;
    private FormatoDigital formato;
    private String urlAcesso;
    private String sinopse;
    private Integer totalAcessos;
    private LocalDate dataCadastro;
    private Long autorId;
    private String autorNome;
    private Long disciplinaId;
    private String disciplinaNome;

    /** Conversão explícita Entidade -> DTO (usada também por Avaliação e Registro de Leitura). */
    public static LivroResponseDTO fromEntity(Livro livro) {
        LivroResponseDTO dto = new LivroResponseDTO();
        dto.setId(livro.getId());
        dto.setTitulo(livro.getTitulo());
        dto.setIsbn(livro.getIsbn());
        dto.setAnoPublicacao(livro.getAnoPublicacao());
        dto.setNumeroPaginas(livro.getNumeroPaginas());
        dto.setTipo(livro.getTipo());
        dto.setFormato(livro.getFormato());
        dto.setUrlAcesso(livro.getUrlAcesso());
        dto.setSinopse(livro.getSinopse());
        dto.setTotalAcessos(livro.getTotalAcessos());
        dto.setDataCadastro(livro.getDataCadastro());
        if (livro.getAutor() != null) {
            dto.setAutorId(livro.getAutor().getId());
            dto.setAutorNome(livro.getAutor().getNome());
        }
        if (livro.getDisciplina() != null) {
            dto.setDisciplinaId(livro.getDisciplina().getId());
            dto.setDisciplinaNome(livro.getDisciplina().getNome());
        }
        return dto;
    }
}
