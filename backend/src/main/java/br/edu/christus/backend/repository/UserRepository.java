package br.edu.christus.backend.repository;

import br.edu.christus.backend.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository
        extends JpaRepository<User, Long> {

}
