package org.example.TesteEndToEnd;

import org.example.TesteEndToEnd.dto.AddressDTO;
import org.example.TesteEndToEnd.dto.UsuarioDTO;
import org.example.TesteEndToEnd.exception.CepInvalidoException;
import org.example.TesteEndToEnd.exception.CepNaoInformadoException;
import org.example.TesteEndToEnd.exception.NomeNaoInformadoException;
import org.example.TesteEndToEnd.model.Usuario;
import org.example.TesteEndToEnd.repository.UsuarioRepository;
import org.example.TesteEndToEnd.service.UsuarioService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@Profile("test")
@ExtendWith(MockitoExtension.class)
public class UsuarioServiceTest {
    private static List<Usuario> usuarios;
    private static Usuario usuario;

    @InjectMocks
    UsuarioService service;

    @Mock
    UsuarioRepository repository;

    @Mock
    RestTemplate restTemplate;

    @BeforeAll
    private static void init() {
        usuarios = new ArrayList<>();
        usuarios.add(new Usuario(1L, "Phoenix", "01234-010", "Rua Nomade", "255", "ap 12", "Sao Paulo", "SP"));
        usuarios.add(new Usuario(2L, "Tempest", "01234-015", "Rua Geleira", "67", null, "Sao Paulo", "SP"));

        usuario = new Usuario(1L, "Phoenix", "01234-010", "Rua Nomade", "255", "ap 12", "Sao Paulo", "SP");
    }

    @Test
    public void whenFindAllUsuarios_ThenAllUsuariosShouldBeFound() {
        when(repository.findAll()).thenReturn(usuarios);

        List<UsuarioDTO> result = service.findAll();
        Assertions.assertEquals(2, result.size());
    }

    @Test
    public void whenGetUsuarioById_ThenUsuarioShouldBeGotten() {
        when(repository.findById(1L)).thenReturn(java.util.Optional.ofNullable(usuario));

        UsuarioDTO usuarioDTO = service.findById(1L);
        Assertions.assertEquals(1L, usuarioDTO.getId());
    }

    @Test
    public void whenUpdateUsuario_ThenUsuarioShouldBeUpdated() {
        when(repository.findById(1L)).thenReturn(java.util.Optional.ofNullable(usuario));
        when(repository.save(usuario)).thenReturn(usuario);

        Assertions.assertDoesNotThrow(() -> {
            UsuarioDTO usuarioDTO = service.update(1L, usuario.toDTO());

            Assertions.assertEquals("Phoenix", usuarioDTO.getNome());
            Assertions.assertEquals("01234-010", usuarioDTO.getCep());
        });
    }

//    @Test
//    public void whenUpdateUsuario_ThenThrowNomeNaoInformadoException() {
//        Assertions.assertThrows(NomeNaoInformadoException.class, () -> {
//            UsuarioDTO result = service.update(1L, new UsuarioDTO(1L, null, "01234-010", "Rua Nomade", "255", "ap 12", "Sao Paulo", "SP"));
//        });
//
//        Assertions.assertThrows(NomeNaoInformadoException.class, () -> {
//            UsuarioDTO result = service.update(1L, new UsuarioDTO(1L, "", "01234-010", "Rua Nomade", "255", "ap 12", "Sao Paulo", "SP"));
//        });
//    }

//    @Test
//    public void whenUpdateUsuario_ThenThrowCepNaoInformadoException() {
//        Assertions.assertThrows(CepNaoInformadoException.class, () -> {
//            UsuarioDTO result = service.update(1L, new UsuarioDTO(1L, "Paulo", "", "Rua Nomade", "255", "ap 12", "Sao Paulo", "SP"));
//        });
//
//        Assertions.assertThrows(CepNaoInformadoException.class, () -> {
//            UsuarioDTO result = service.update(1L, new UsuarioDTO(1L, "Paulo", null, "Rua Nomade", "255", "ap 12", "Sao Paulo", "SP"));
//        });
//    }

    @Test
    public void whenCreateUsuario_ThenUsuarioShouldBeCreated() {
        usuario = new Usuario(1L, "Phoenix", "01234-010", "Praça Charles Miller", "255", "ap 12", "Sao Paulo", "SP");
        AddressDTO addressDTO = new AddressDTO("01234-010", "Praça Charles Miller", "", "Pacaembu", "Sao Paulo", "SP", "3550308", "1004", "11", "7107", false);
        when(repository.save(usuario)).thenReturn(usuario);

        when(restTemplate.getForEntity("https://viacep.com.br/ws/01234010/json/", AddressDTO.class)).thenReturn(new ResponseEntity(addressDTO, HttpStatus.OK));

        Assertions.assertDoesNotThrow(() -> {
            UsuarioDTO result = service.create(new UsuarioDTO(1L, "Phoenix", "01234-010", "Rua Nomade", "255", "ap 12", "Sao Paulo", "SP"));
            Assertions.assertEquals(1L, result.getId());
            Assertions.assertEquals("Phoenix", result.getNome());
            Assertions.assertEquals("Praça Charles Miller", result.getLogradouro());
        });
    }

//    @Test
//    public void whenCreateUsuario_ThenThrowNomeNaoInformadoException() {
//        Assertions.assertThrows(NomeNaoInformadoException.class, () -> {
//            UsuarioDTO result = service.create(new UsuarioDTO(1L, null, "01234-010", "Rua Nomade", "255", "ap 12", "Sao Paulo", "SP"));
//        });
//
//        Assertions.assertThrows(NomeNaoInformadoException.class, () -> {
//            UsuarioDTO result = service.create(new UsuarioDTO(1L, "", "01234-010", "Rua Nomade", "255", "ap 12", "Sao Paulo", "SP"));
//        });
//    }

//    @Test
//    public void whenCreateUsuario_ThenThrowCepNaoInformadoException() {
//        Assertions.assertThrows(CepNaoInformadoException.class, () -> {
//            UsuarioDTO result = service.create(new UsuarioDTO(1L, "Snider", "", "Rua Nomade", "255", "ap 12", "Sao Paulo", "SP"));
//        });
//
//        Assertions.assertThrows(CepNaoInformadoException.class, () -> {
//            UsuarioDTO result = service.create(new UsuarioDTO(1L, "Snider", null, "Rua Nomade", "255", "ap 12", "Sao Paulo", "SP"));
//        });
//    }

    @Test
    public void whenDeleteUsuario_ThenUsuarioShouldBeDeleted() {
        when(repository.findById(1L)).thenReturn(java.util.Optional.ofNullable(usuario));

        service.delete(1L);

        Mockito.verify(repository, times(1)).findById(1L);
        Mockito.verify(repository, times(1)).deleteById(1L);
    }

    @Test
    public void whenCreateUsuario_ThenThrowCepInvalidoException() {
        AddressDTO addressDTO = new AddressDTO();
        when(restTemplate.getForEntity("https://viacep.com.br/ws/00000000/json/", AddressDTO.class)).thenReturn(new ResponseEntity(addressDTO, HttpStatus.OK));

        Assertions.assertThrows(CepInvalidoException.class, () -> {
            UsuarioDTO result = service.create(new UsuarioDTO(1L, "Snider", "00000000", "Rua Nomade", "255", "ap 12", "Sao Paulo", "SP"));
        });
    }
}
