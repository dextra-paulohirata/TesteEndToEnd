package org.example.TesteEndToEnd.controller;

import org.example.TesteEndToEnd.dto.UsuarioDTO;
import org.example.TesteEndToEnd.exception.CepNaoInformadoException;
import org.example.TesteEndToEnd.exception.NomeNaoInformadoException;
import org.example.TesteEndToEnd.service.UsuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping({"/usuarios"})
public class UsuarioController {

    private UsuarioService service;

    UsuarioController(UsuarioService usuarioService) {
        this.service = usuarioService;
    }

    @GetMapping
    public List<UsuarioDTO> findAll() {
        return service.findAll();
    }

    @GetMapping(path = {"/{id}"})
    public ResponseEntity<UsuarioDTO> findById(@PathVariable long id){
        UsuarioDTO usuarioDTO = service.findById(id);
        if (usuarioDTO == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(usuarioDTO);
        }
    }

    @PostMapping
    // TODO: criar uma exception padrao para retornar no Rest
    public UsuarioDTO create(@RequestBody UsuarioDTO usuarioDTO) throws NomeNaoInformadoException, CepNaoInformadoException {
        return service.create(usuarioDTO);
    }

    @PutMapping(value="/{id}")
    public ResponseEntity<UsuarioDTO> update(@PathVariable("id") long id,
                                          @RequestBody UsuarioDTO usuarioDTO) throws NomeNaoInformadoException, CepNaoInformadoException {
        UsuarioDTO usuarioAtualizado = service.update(id, usuarioDTO);
        if (usuarioAtualizado == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(usuarioAtualizado);
        }
    }

    @DeleteMapping(path = {"/{id}"})
    public ResponseEntity<?> delete(@PathVariable("id") long id) {
        if(service.delete(id)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}