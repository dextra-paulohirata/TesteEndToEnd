package org.example.TesteEndToEnd.controller;

import org.example.TesteEndToEnd.dto.UsuarioDTO;
import org.example.TesteEndToEnd.exception.CepInvalidoException;
import org.example.TesteEndToEnd.exception.CepNaoInformadoException;
import org.example.TesteEndToEnd.exception.NomeNaoInformadoException;
import org.example.TesteEndToEnd.service.UsuarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

@RestController
@RequestMapping({"/usuarios"})
@Validated
public class UsuarioController {

    private UsuarioService service;

    UsuarioController(UsuarioService usuarioService) {
        this.service = usuarioService;
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ResponseEntity<String> handleConstraintViolationException(ConstraintViolationException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @GetMapping
    public List<UsuarioDTO> findAll() {
        return service.findAll();
    }

    @GetMapping(path = {"/{id}"})
    public ResponseEntity<UsuarioDTO> findById(@PathVariable @Min(value = 0, message = "PathVariable id deve ser maior que zero") long id){
        UsuarioDTO usuarioDTO = service.findById(id);
        if (usuarioDTO == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(usuarioDTO);
        }
    }

    @PostMapping
    // TODO: criar uma exception padrao para retornar no Rest
    public ResponseEntity<UsuarioDTO> create(@Valid @RequestBody UsuarioDTO usuarioDTO) throws NomeNaoInformadoException, CepNaoInformadoException, CepInvalidoException {
        return ResponseEntity.ok(service.create(usuarioDTO));
    }

    @PutMapping(value="/{id}")
    public ResponseEntity<UsuarioDTO> update(@PathVariable("id") @Min(value = 0, message = "PathVariable id deve ser maior que zero") long id,
                                             @Valid @RequestBody UsuarioDTO usuarioDTO) throws NomeNaoInformadoException, CepNaoInformadoException, CepInvalidoException {
        UsuarioDTO usuarioAtualizado = service.update(id, usuarioDTO);
        if (usuarioAtualizado == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(usuarioAtualizado);
        }
    }

    @DeleteMapping(path = {"/{id}"})
    public ResponseEntity<?> delete(@PathVariable("id") @Min(value = 0, message = "PathVariable id deve ser maior que zero") long id) {
        if(service.delete(id)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}