package org.example.TesteEndToEnd.service;

import org.example.TesteEndToEnd.dto.AddressDTO;
import org.example.TesteEndToEnd.dto.UsuarioDTO;
import org.example.TesteEndToEnd.exception.CepInvalidoException;
import org.example.TesteEndToEnd.exception.CepNaoInformadoException;
import org.example.TesteEndToEnd.exception.NomeNaoInformadoException;
import org.example.TesteEndToEnd.model.Usuario;
import org.example.TesteEndToEnd.repository.UsuarioRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {
    public static final String URL_BUSCA_CEP = "https://viacep.com.br/ws/%s/json/";
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
        getAddressFromViaCep(dto);
        Usuario usuarioCreated = repository.save(dto.toModel());
        return usuarioCreated.toDTO();
    }

    public UsuarioDTO update(long id, UsuarioDTO dto) throws NomeNaoInformadoException, CepNaoInformadoException, CepInvalidoException {
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

        if (!optionalUsuario.isPresent()) {
            return false;
        }
        repository.deleteById(id);
        return true;
    }

    private void getAddressFromViaCep(UsuarioDTO dto) throws CepInvalidoException {
        AddressDTO addressDTO = searchZIP(dto.getCep());
        dto.setCidade(addressDTO.getLocalidade());
        dto.setLogradouro(addressDTO.getLogradouro());
        dto.setEstado(addressDTO.getUf());
    }

    private AddressDTO searchZIP(String cep) throws CepInvalidoException {
        String cepTratado = cep.replace("-", "").trim();
        String url = String.format(URL_BUSCA_CEP, cepTratado);
        ResponseEntity<AddressDTO> addressDTO = restTemplate.getForEntity(url, AddressDTO.class);
        if (addressDTO.getBody() == null || (addressDTO.getBody().getErro() != null && addressDTO.getBody().getErro()) || addressDTO.getBody().getCep() == null) {
            throw new CepInvalidoException();
        }
        return addressDTO.getBody();
    }
}
