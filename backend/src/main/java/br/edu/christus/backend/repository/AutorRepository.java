package br.edu.christus.backend.repository;

import br.edu.christus.backend.domain.model.Autor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AutorRepository extends JpaRepository<Autor, Long> {

    // Consulta derivada do nome do método: WHERE UPPER(nome) LIKE UPPER('%texto%')
    List<Autor> findByNomeContainingIgnoreCase(String nome);
}
