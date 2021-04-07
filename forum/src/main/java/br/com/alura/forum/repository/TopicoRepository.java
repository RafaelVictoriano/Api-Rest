package br.com.alura.forum.repository;

import br.com.alura.forum.modelo.Topico;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;

@Repository
public interface TopicoRepository extends JpaRepository<Topico, Long> {

    Page<Topico> findByCursoNome(String nomeCurso, Pageable pageable);

}
