package org.example.TesteEndToEnd;

import org.example.TesteEndToEnd.config.MySqlConfig;
import org.example.TesteEndToEnd.model.Usuario;
import org.example.TesteEndToEnd.repository.UsuarioRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

@SpringBootTest(classes = { MySqlConfig.class })
@ActiveProfiles("test")
public class TesteEndToEndMySqlTests {
    @Autowired
    private UsuarioRepository repository;

    @Test
    public void givenUsuarioRepository_whenSaveAndRetrieveEntity_thenOK() {
        Usuario savedEntity = repository.save(new Usuario(1L, "Phoenix", "01234-010", "Rua Nomade", "255", "ap 12", "Sao Paulo", "SP"));
        Assertions.assertEquals(1L, savedEntity.getId());

        Optional<Usuario> optional = repository.findById(savedEntity.getId());
        Assertions.assertEquals(true, optional.isPresent());
    }

}
