package br.edu.christus.backend.repository;

import br.edu.christus.backend.domain.model.Disciplina;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DisciplinaRepository extends JpaRepository<Disciplina, Long> {

    List<Disciplina> findBySerie(Integer serie);

    // Usado no cadastro para impedir nomes repetidos
    boolean existsByNomeIgnoreCase(String nome);

    // Usado na edição: existe OUTRA disciplina (id diferente) com esse nome?
    boolean existsByNomeIgnoreCaseAndIdNot(String nome, Long id);
}
