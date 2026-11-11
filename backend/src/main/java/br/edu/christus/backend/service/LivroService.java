package br.edu.christus.backend.service;

import br.edu.christus.backend.domain.dto.LivroRequestDTO;
import br.edu.christus.backend.domain.dto.LivroResponseDTO;
import br.edu.christus.backend.domain.model.Livro;
import br.edu.christus.backend.repository.AvaliacaoRepository;
import br.edu.christus.backend.repository.LivroRepository;
import br.edu.christus.backend.repository.RegistroLeituraRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.Year;
import java.util.List;

@Service
public class LivroService {

    @Autowired
    private LivroRepository livroRepository;

    @Autowired
    private AutorService autorService;

    @Autowired
    private DisciplinaService disciplinaService;

    @Autowired
    private AvaliacaoRepository avaliacaoRepository;

    @Autowired
    private RegistroLeituraRepository registroLeituraRepository;

    public LivroResponseDTO create(LivroRequestDTO dto) {
        String isbn = normalizarIsbn(dto.getIsbn());
        validarAnoPublicacao(dto.getAnoPublicacao());
        if (isbn != null && livroRepository.existsByIsbn(isbn)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Já existe um material cadastrado com o ISBN: " + isbn);
        }

        Livro livro = new Livro();
        preencherDados(livro, dto, isbn);
        livro.setTotalAcessos(0);
        livro.setDataCadastro(LocalDate.now());

        return LivroResponseDTO.fromEntity(livroRepository.save(livro));
    }

    public List<LivroResponseDTO> findAll(String titulo, Long autorId, Long disciplinaId) {
        List<Livro> lista;
        if (titulo != null && !titulo.isBlank()) {
            lista = livroRepository.findByTituloContainingIgnoreCase(titulo);
        } else if (autorId != null) {
            lista = livroRepository.findByAutorId(autorId);
        } else if (disciplinaId != null) {
            lista = livroRepository.findByDisciplinaId(disciplinaId);
        } else {
            lista = livroRepository.findAll();
        }
        return lista.stream().map(LivroResponseDTO::fromEntity).toList();
    }

    public LivroResponseDTO findById(Long id) {
        return LivroResponseDTO.fromEntity(buscarLivro(id));
    }

    public LivroResponseDTO update(Long id, LivroRequestDTO dto) {
        Livro livro = buscarLivro(id);
        String isbn = normalizarIsbn(dto.getIsbn());
        validarAnoPublicacao(dto.getAnoPublicacao());
        if (isbn != null && livroRepository.existsByIsbnAndIdNot(isbn, id)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Já existe outro material cadastrado com o ISBN: " + isbn);
        }
        preencherDados(livro, dto, isbn);
        return LivroResponseDTO.fromEntity(livroRepository.save(livro));
    }

    public void delete(Long id) {
        buscarLivro(id);
        if (!avaliacaoRepository.findByLivroId(id).isEmpty()
                || !registroLeituraRepository.findByLivroId(id).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Não é possível excluir o material de ID " + id
                            + " porque ele possui avaliações ou registros de leitura vinculados.");
        }
        livroRepository.deleteById(id);
    }

    /** Registra que um estudante abriu o material: soma 1 ao contador de acessos. */
    public LivroResponseDTO registrarAcesso(Long id) {
        Livro livro = buscarLivro(id);
        livro.setTotalAcessos(livro.getTotalAcessos() + 1);
        return LivroResponseDTO.fromEntity(livroRepository.save(livro));
    }

    /** Os 5 materiais mais acessados do acervo. */
    public List<LivroResponseDTO> maisAcessados() {
        return livroRepository.findTop5ByOrderByTotalAcessosDesc()
                .stream().map(LivroResponseDTO::fromEntity).toList();
    }

    public Livro buscarLivro(Long id) {
        return livroRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Livro/material não encontrado com o ID: " + id));
    }

    // ---------- métodos auxiliares ----------

    private void preencherDados(Livro livro, LivroRequestDTO dto, String isbn) {
        livro.setTitulo(dto.getTitulo());
        livro.setIsbn(isbn);
        livro.setAnoPublicacao(dto.getAnoPublicacao());
        livro.setNumeroPaginas(dto.getNumeroPaginas());
        livro.setTipo(dto.getTipo());
        livro.setFormato(dto.getFormato());
        livro.setUrlAcesso(dto.getUrlAcesso());
        livro.setSinopse(dto.getSinopse());
        // Relacionamentos: busca Autor e Disciplina pelo ID (404 se não existirem)
        livro.setAutor(autorService.buscarAutor(dto.getAutorId()));
        livro.setDisciplina(disciplinaService.buscarDisciplina(dto.getDisciplinaId()));
    }

    // Regra que a anotação não consegue expressar: o ano não pode estar no futuro
    private void validarAnoPublicacao(Integer ano) {
        int anoAtual = Year.now().getValue();
        if (ano > anoAtual) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "O ano de publicação (" + ano + ") não pode ser maior que o ano atual (" + anoAtual + ")");
        }
    }

    private String normalizarIsbn(String isbn) {
        return (isbn == null || isbn.isBlank()) ? null : isbn.trim();
    }
}
