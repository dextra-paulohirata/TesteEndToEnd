package org.example.TesteEndToEnd.service;

import org.example.TesteEndToEnd.dto.UsuarioDTO;
import org.example.TesteEndToEnd.exception.CepNaoInformadoException;
import org.example.TesteEndToEnd.exception.NomeNaoInformadoException;
import org.example.TesteEndToEnd.model.Usuario;
import org.example.TesteEndToEnd.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {
    private UsuarioRepository repository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.repository = usuarioRepository;
    }

    public List findAll(){
        List<Usuario> usuarios = repository.findAll();
        List<UsuarioDTO> usuariosDTO = new ArrayList<>();
        for (Usuario usuario : usuarios) {
            usuariosDTO.add(usuario.toDTO());
        }
        return usuariosDTO;
    }

    public UsuarioDTO findById(long id) {
        Optional<Usuario> optionalUsuario = repository.findById(id);
        return optionalUsuario.isPresent() ? optionalUsuario.get().toDTO() : null;
    }

    public UsuarioDTO create(UsuarioDTO dto) throws NomeNaoInformadoException, CepNaoInformadoException {
        validateDTO(dto);
        Usuario usuarioCreated = repository.save(dto.toModel());
        return usuarioCreated.toDTO();
    }

    public UsuarioDTO update(long id, UsuarioDTO dto) throws NomeNaoInformadoException, CepNaoInformadoException {
        validateDTO(dto);

        Optional<Usuario> optionalUsuario = repository.findById(id);
        Usuario usuarioAtualizado = optionalUsuario
                                        .map(usuario -> {
                                            usuario.setNome(dto.getNome());
                                            usuario.setCep(dto.getCep());
                                            usuario.setLogradouro(dto.getLogradouro());
                                            usuario.setNumero(dto.getNumero());
                                            usuario.setComplemento(dto.getComplemento());
                                            usuario.setCidade(dto.getCidade());
                                            usuario.setEstado(dto.getEstado());
                                            return repository.save(usuario);
                                        }).orElse(null);
        return usuarioAtualizado == null ? null : usuarioAtualizado.toDTO();
    }

    private void validateDTO (UsuarioDTO dto) throws NomeNaoInformadoException, CepNaoInformadoException {
        if (dto.getNome() == null || dto.getNome().isEmpty()) {
            throw new NomeNaoInformadoException();
        }

        if (dto.getCep() == null || dto.getCep().isEmpty()) {
            throw new CepNaoInformadoException();
        }
    }

    public boolean delete(long id) {
        Optional<Usuario> optionalUsuario = repository.findById(id);
        if (optionalUsuario.isPresent()) {
            repository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }
}
