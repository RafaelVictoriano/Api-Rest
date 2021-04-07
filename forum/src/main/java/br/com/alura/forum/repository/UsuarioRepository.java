package br.com.alura.forum.repository;

import br.com.alura.forum.modelo.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends  JpaRepository<Usuario, Long> {

    Optional<Usuario> findByEmail(String username);
}
