package br.edu.christus.backend.repository;

import br.edu.christus.backend.domain.model.Avaliacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AvaliacaoRepository extends JpaRepository<Avaliacao, Long> {

    List<Avaliacao> findByLivroId(Long livroId);

    List<Avaliacao> findByUsuarioId(Long usuarioId);

    boolean existsByUsuarioIdAndLivroId(Long usuarioId, Long livroId);
}
