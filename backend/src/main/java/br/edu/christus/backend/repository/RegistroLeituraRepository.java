package br.edu.christus.backend.repository;

import br.edu.christus.backend.domain.model.RegistroLeitura;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RegistroLeituraRepository extends JpaRepository<RegistroLeitura, Long> {

    List<RegistroLeitura> findByUsuarioId(Long usuarioId);

    List<RegistroLeitura> findByLivroId(Long livroId);

    Optional<RegistroLeitura> findByUsuarioIdAndLivroId(Long usuarioId, Long livroId);

    boolean existsByUsuarioIdAndLivroId(Long usuarioId, Long livroId);
}
