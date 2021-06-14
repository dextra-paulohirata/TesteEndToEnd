package org.example.TesteEndToEnd;

import io.restassured.RestAssured;

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
    public void usuario_controller_returns_200_with_expected_usuario() {
        when().get("/usuarios/{id}", 1).
                then().
                statusCode(200).
                body("id", equalTo(1),
                        "nome", equalTo("Phoenix"));
    }

    @Test
    public void usuario_controller_returns_200_with_all_usuario() {
        when().get("/usuarios").
                then().
                statusCode(200).body("", hasSize(1));
    }
}
