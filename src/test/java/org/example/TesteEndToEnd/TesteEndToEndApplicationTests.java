package org.example.TesteEndToEnd;

import com.google.gson.Gson;
import io.restassured.RestAssured;
import org.example.TesteEndToEnd.dto.UsuarioDTO;
import org.example.TesteEndToEnd.exception.CepInvalidoException;
import org.example.TesteEndToEnd.exception.CepNaoInformadoException;
import org.example.TesteEndToEnd.exception.NomeNaoInformadoException;
import org.example.TesteEndToEnd.model.Usuario;
import org.example.TesteEndToEnd.repository.UsuarioRepository;
import org.example.TesteEndToEnd.service.UsuarioService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;
import java.util.Optional;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;

@Sql(scripts = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@Profile("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ContextConfiguration(classes = TesteEndToEndApplication.class)
class TesteEndToEndApplicationTests {

	@Autowired
	private UsuarioRepository repository;

	@Autowired
	private UsuarioService service;

	@LocalServerPort
	private int port;

	@BeforeEach
	public void initialize() {
		RestAssured.port = port;
	}

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
		} catch (NomeNaoInformadoException | CepNaoInformadoException | CepInvalidoException e) {
			e.printStackTrace();
		}
		List<UsuarioDTO> result = service.findAll();
		Assertions.assertEquals(2, result.size());
	}

	@Test
	public void givenUsuarioThenSavesUsuarioAndDelete() {
		String usuarioCreated = given().contentType("application/json")
				.body(new UsuarioDTO(2L, "Typhon", "01500-060", "Rua Vazia", "144", "", "Sao Paulo", "SP"))
				.when().post("usuarios").asString();

		Gson gson = new Gson();
		UsuarioDTO dto = gson.fromJson(usuarioCreated, UsuarioDTO.class);

//		when().delete("/usuarios/{id}", dto.getId()).then().statusCode(200);

		Assertions.assertEquals("Typhon", dto.getNome());
		Assertions.assertEquals("Rua Vazia", dto.getLogradouro());
	}

	@Test
	public void givenUsuarioThenFindByIdAndDelete() {
		String usuarioCreated = given().contentType("application/json")
				.body(new UsuarioDTO(1L, "Phoenix", "01234-010", "Rua Nomade", "365", "ap 12", "Sao Paulo", "SP"))
				.when().post("usuarios").asString();

		Gson gson = new Gson();
		UsuarioDTO dto = gson.fromJson(usuarioCreated, UsuarioDTO.class);

		when().get("/usuarios/{id}", dto.getId()).
				then().
				statusCode(200).
				body("nome", equalTo(dto.getNome()));

//		when().delete("/usuarios/{id}", dto.getId()).then().statusCode(200);
	}

	@Test
	public void givenUsuarioThenFindAllUsuarioAndDelete() {
		String usuarioCreated = given().contentType("application/json")
				.body(new UsuarioDTO(1L, "Dallas", "02458-000", "Av Prates", "740", "ap 104", "Sao Paulo", "SP"))
				.when().post("usuarios").asString();

		Gson gson = new Gson();
		UsuarioDTO dto = gson.fromJson(usuarioCreated, UsuarioDTO.class);

		when().get("/usuarios").
				then().
				statusCode(200).body("", hasSize(1));

//		when().delete("/usuarios/{id}", dto.getId()).then().statusCode(200);
	}
}
