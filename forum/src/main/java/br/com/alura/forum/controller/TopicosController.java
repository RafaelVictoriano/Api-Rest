package br.com.alura.forum.controller;

import br.com.alura.forum.form.AtualizacaoTopicoForm;
import br.com.alura.forum.Dto.DatalhesTopicoDto;
import br.com.alura.forum.Dto.TopicoDto;

import br.com.alura.forum.form.TopicoForm;
import br.com.alura.forum.modelo.Topico;
import br.com.alura.forum.repository.CursoRepository;
import br.com.alura.forum.repository.TopicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.net.URI;
import java.util.Optional;


@RestController
@RequestMapping("/topicos")
public class TopicosController {

    @Autowired
    private TopicoRepository topicoRepository;

    @Autowired
    private CursoRepository cursoRepository;

    @GetMapping
    @Cacheable(value = "listaTopicos")
    public Page<TopicoDto> lista(@RequestParam(required = false) String nomeCurso, @PageableDefault(sort ="id", direction = Sort.Direction.DESC, page = 0, size = 5) Pageable page) {

        if (nomeCurso == null) {
            Page<Topico> topico = topicoRepository.findAll(page);
            return TopicoDto.converter(topico);
        } else {
            Page<Topico> topicos = topicoRepository.findByCursoNome(nomeCurso, page);
            return TopicoDto.converter(topicos);
        }
    }

    @PostMapping
    @Transactional
    @CacheEvict(value = "listaTopicos", allEntries = true) //allEntries faz com que ele reincie todos apos o cadastro
    public ResponseEntity<TopicoDto> cadastrar(@RequestBody @Valid TopicoForm form, UriComponentsBuilder uri) { //pega do corpo da requisição
        Topico topico = form.converter(cursoRepository);
        topicoRepository.save(topico);

        URI uri1 = uri.path("/topicos/{id}").buildAndExpand(topico.getId()).toUri();// path manda o endereço e o build espand   e e passa o id para o enedereço;

        return ResponseEntity.created(uri1).body(new TopicoDto(topico));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DatalhesTopicoDto> datalhar(@PathVariable("id") Long id) {

        Optional<Topico> topico = topicoRepository.findById(id);
        if (topico.isPresent()) {
            return ResponseEntity.ok(new DatalhesTopicoDto(topico.get()));
        }

        return ResponseEntity.notFound().build();

    }

    @PutMapping("/{id}")
    @Transactional //Garantir que ele faça a transação no fim do metodo
    @CacheEvict(value = "listaTopicos", allEntries = true)
    public ResponseEntity<TopicoDto> atualizar(@PathVariable Long id, @RequestBody @Valid AtualizacaoTopicoForm form) {

        Optional<Topico> optinal = topicoRepository.findById(id);
        if (optinal.isPresent()) {
            Topico topico = form.atualizar(id, topicoRepository);
            return ResponseEntity.ok(new TopicoDto(topico));
        }

        return ResponseEntity.notFound().build();

    }

    @DeleteMapping("/{id}")
    @Transactional
    @CacheEvict(value = "listaTopicos", allEntries = true)
    public ResponseEntity<?> remover(@PathVariable Long id) {

        Optional<Topico> optinal = topicoRepository.findById(id);
        if (optinal.isPresent()) {
            topicoRepository.deleteById(id);
            return ResponseEntity.ok().build();
        }

        return ResponseEntity.notFound().build();

    }


}
