package br.com.alura.forum.service;

import br.com.alura.forum.Dto.DatalhesTopicoDto;
import br.com.alura.forum.Dto.TopicoDto;
import br.com.alura.forum.form.AtualizacaoTopicoForm;
import br.com.alura.forum.form.TopicoForm;
import br.com.alura.forum.modelo.Topico;
import br.com.alura.forum.repository.CursoRepository;
import br.com.alura.forum.repository.TopicoRepository;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.util.Optional;

@Service
public class TopicService {

    @Autowired
    private TopicoRepository topicoRepository;
    @Autowired
    private CursoRepository cursoRepository;

    public Page<TopicoDto> findAll(String nomeCurso, Pageable page) {
        Page<Topico> topico;
        if (nomeCurso == null) {
            topico = topicoRepository.findAll(page);
            return TopicoDto.converter(topico);
        }
        topico = topicoRepository.findByCursoNome(nomeCurso, page);
        return TopicoDto.converter(topico);
    }

    public TopicoDto cadastrar(TopicoForm form) {
        Topico topico = form.converter(cursoRepository);
        topicoRepository.save(topico);
        return new TopicoDto(topico);
    }

    public DatalhesTopicoDto datalhar(Long id) throws NotFoundException {
        Optional<Topico> topico = topicoRepository.findById(id);
        topico.orElseThrow(() -> new NotFoundException("Topico não encotrado"));
        return new DatalhesTopicoDto(topico.get());
    }

    public TopicoDto atualizar(Long id, AtualizacaoTopicoForm form) throws NotFoundException {
        Optional<Topico> optional = topicoRepository.findById(id);
        optional.orElseThrow(() -> new NotFoundException("Topico não encontrado"));
        Topico topico = form.atualizar(id, topicoRepository);
        return new TopicoDto(topico);
    }

    public void remover(Long id) throws NotFoundException {
        Optional<Topico> optional = topicoRepository.findById(id);
        optional.orElseThrow(() -> new NotFoundException("Topico não encontrado"));
        topicoRepository.deleteById(id);
    }
}
