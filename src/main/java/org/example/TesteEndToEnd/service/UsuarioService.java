package org.example.TesteEndToEnd.service;

import org.example.TesteEndToEnd.dto.AddressDTO;
import org.example.TesteEndToEnd.dto.UsuarioDTO;
import org.example.TesteEndToEnd.exception.CepInvalidoException;
import org.example.TesteEndToEnd.exception.CepNaoInformadoException;
import org.example.TesteEndToEnd.exception.NomeNaoInformadoException;
import org.example.TesteEndToEnd.model.Usuario;
import org.example.TesteEndToEnd.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {
    private UsuarioRepository repository;

    private RestTemplate restTemplate;

    public UsuarioService(UsuarioRepository usuarioRepository, RestTemplate restTemplate) {
        this.repository = usuarioRepository;
        this.restTemplate = restTemplate;
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

    public UsuarioDTO create(UsuarioDTO dto) throws NomeNaoInformadoException, CepNaoInformadoException, CepInvalidoException {
        validateDTO(dto);
        getAddressFromViaCep(dto);
        Usuario usuarioCreated = repository.save(dto.toModel());
        return usuarioCreated.toDTO();
    }

    public UsuarioDTO update(long id, UsuarioDTO dto) throws NomeNaoInformadoException, CepNaoInformadoException, CepInvalidoException {
        validateDTO(dto);

        Optional<Usuario> optionalUsuario = repository.findById(id);
        if (optionalUsuario.isPresent()) {
            if (!optionalUsuario.get().getCep().equalsIgnoreCase(dto.getCep())) {
                getAddressFromViaCep(dto);
            }
        }

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

    private void getAddressFromViaCep(UsuarioDTO dto) throws CepInvalidoException {
        AddressDTO addressDTO = searchZIP(dto.getCep());
        dto.setCidade(addressDTO.getLocalidade());
        dto.setLogradouro(addressDTO.getLogradouro());
        dto.setEstado(addressDTO.getUf());
    }

    private AddressDTO searchZIP(String cep) throws CepInvalidoException {
        String url = "https://viacep.com.br/ws/" + cep.replace("-", "").replace(" ", "");
        url += "/json/";
        ResponseEntity<AddressDTO> addressDTO = restTemplate.getForEntity(url, AddressDTO.class);
        if (addressDTO.getBody() == null || addressDTO.getBody().getCep() == null) {
            throw new CepInvalidoException();
        }
        return addressDTO.getBody();
    }
}
