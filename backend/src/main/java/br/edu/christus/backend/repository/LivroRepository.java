package br.edu.christus.backend.repository;

import br.edu.christus.backend.domain.model.Livro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LivroRepository extends JpaRepository<Livro, Long> {

    // Filtros da listagem
    List<Livro> findByTituloContainingIgnoreCase(String titulo);

    List<Livro> findByAutorId(Long autorId);

    List<Livro> findByDisciplinaId(Long disciplinaId);

    // Ranking: os 5 materiais com mais acessos
    List<Livro> findTop5ByOrderByTotalAcessosDesc();

    // Verificações usadas antes de excluir Autor/Disciplina
    boolean existsByAutorId(Long autorId);

    boolean existsByDisciplinaId(Long disciplinaId);

    // Verificações de ISBN repetido (cadastro e edição)
    boolean existsByIsbn(String isbn);

    boolean existsByIsbnAndIdNot(String isbn, Long id);
}
