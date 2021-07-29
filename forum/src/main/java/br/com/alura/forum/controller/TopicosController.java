package br.com.alura.forum.controller;

import br.com.alura.forum.form.AtualizacaoTopicoForm;
import br.com.alura.forum.Dto.DatalhesTopicoDto;
import br.com.alura.forum.Dto.TopicoDto;

import br.com.alura.forum.form.TopicoForm;
import br.com.alura.forum.modelo.Topico;
import br.com.alura.forum.repository.CursoRepository;
import br.com.alura.forum.repository.TopicoRepository;
import br.com.alura.forum.service.TopicService;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
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
    private TopicService topicoService;

    @GetMapping
    @Cacheable(value = "listaTopicos")
    public Page<TopicoDto> lista(@RequestParam(required = false) String nomeCurso, @PageableDefault(sort = "id", direction = Sort.Direction.DESC, page = 0, size = 5) Pageable page) {
        return topicoService.findAll(nomeCurso, page);
    }

    @PostMapping
    @Transactional
    @CacheEvict(value = "listaTopicos", allEntries = true) //allEntries faz com que ele reincie todos apos o cadastro
    public ResponseEntity<TopicoDto> cadastrar(@RequestBody @Valid TopicoForm form, UriComponentsBuilder uri) { //pega do corpo da requisição
        TopicoDto topicoDto = topicoService.cadastrar(form);
        return new ResponseEntity<>(topicoDto, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DatalhesTopicoDto> datalhar(@PathVariable("id") Long id) throws NotFoundException {
        DatalhesTopicoDto detalhar = topicoService.datalhar(id);
        return ResponseEntity.ok(detalhar);
    }

    @PutMapping("/{id}")
    @Transactional //Garantir que ele faça a transação no fim do metodo
    @CacheEvict(value = "listaTopicos", allEntries = true)
    public ResponseEntity<TopicoDto> atualizar(@PathVariable Long id, @RequestBody @Valid AtualizacaoTopicoForm form) throws NotFoundException {
        TopicoDto topicoAtualizado = topicoService.atualizar(id, form);
        return ResponseEntity.ok(topicoAtualizado);
    }

    @DeleteMapping("/{id}")
    @Transactional
    @CacheEvict(value = "listaTopicos", allEntries = true)
    public ResponseEntity<?> remover(@PathVariable Long id) throws NotFoundException {
        topicoService.remover(id);
        return ResponseEntity.ok().build();
    }


}
