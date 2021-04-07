package br.com.alura.forum.form;

import br.com.alura.forum.modelo.Curso;
import br.com.alura.forum.modelo.Topico;
import br.com.alura.forum.repository.CursoRepository;
import com.sun.istack.NotNull;
import javax.validation.constraints.NotEmpty;

import lombok.Data;
import org.hibernate.validator.constraints.Length;


@Data
public class TopicoForm {

    @NotNull @NotEmpty @Length(min = 5)
    private String titulo;

    @NotNull @NotEmpty @Length(min = 10)
    private String mensagem;

    @NotNull @NotEmpty
    private String nomeCurso;

    public Topico converter(CursoRepository cursoRepository) {
       Curso nome = cursoRepository.findByNome(nomeCurso);
       return new Topico(this.titulo, this.mensagem, nome );
    }
}


