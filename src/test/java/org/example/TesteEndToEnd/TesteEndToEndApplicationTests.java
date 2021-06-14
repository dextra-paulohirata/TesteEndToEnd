package org.example.TesteEndToEnd;

import org.example.TesteEndToEnd.dto.UsuarioDTO;
import org.example.TesteEndToEnd.exception.CepNaoInformadoException;
import org.example.TesteEndToEnd.exception.NomeNaoInformadoException;
import org.example.TesteEndToEnd.model.Usuario;
import org.example.TesteEndToEnd.repository.UsuarioRepository;
import org.example.TesteEndToEnd.service.UsuarioService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

@SpringBootTest
class TesteEndToEndApplicationTests {

	@Autowired
	private UsuarioRepository repository;

	@Autowired
	private UsuarioService service;

	@Test
	public void givenUsuarioRepository_whenSaveAndRetrieveEntity_thenOK() {
		Usuario savedEntity = repository.save(new Usuario(1L, "Phoenix", "01234-010", "Rua Nomade", "255", "ap 12", "Sao Paulo", "SP"));
		Assertions.assertEquals(1L, savedEntity.getId());

		Optional<Usuario> optional = repository.findById(savedEntity.getId());
		Assertions.assertEquals(true, optional.isPresent());
	}

	@Test
	public void givenUsuariosCreated_whenFindAllUsuarios_ThenAllUsuariosCreatedShouldBeFound(){
		try {
			service.create(new UsuarioDTO(1L, "Phoenix", "01234-010", "Rua Nomade", "255", "ap 12", "Sao Paulo", "SP"));
			service.create(new UsuarioDTO(2L, "Nemesis", "43210-010", "Rua Montes Claros", "53", "ap 12", "Diamantina", "MG"));
		} catch (NomeNaoInformadoException e) {
			e.printStackTrace();
		} catch (CepNaoInformadoException e) {
			e.printStackTrace();
		}
		List<UsuarioDTO> result = service.findAll();
		Assertions.assertEquals(2, result.size());
	}
}
