package org.example.TesteEndToEnd;

import com.google.gson.Gson;
import io.restassured.RestAssured;

import org.example.TesteEndToEnd.dto.UsuarioDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;

@SpringBootTest
public class TesteEndToEndRestAssured {

    @BeforeAll
    public static void setup() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 7090;
    }

    @Test
    public void givenUsuarioThenSavesUsuarioAndDelete() {
        String usuarioCreated = given().contentType("application/json")
                .body(new UsuarioDTO(2L, "Typhon", "01500-060", "Rua Vazia", "144", "", "Sao Paulo", "SP"))
                .when().post("usuarios").asString();

        Gson gson = new Gson();
        UsuarioDTO dto = gson.fromJson(usuarioCreated, UsuarioDTO.class);

        when().delete("/usuarios/{id}", dto.getId()).then().statusCode(200);

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

        when().delete("/usuarios/{id}", dto.getId()).
                then().statusCode(200);
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

        when().delete("/usuarios/{id}", dto.getId()).
                then().statusCode(200);
    }


}
