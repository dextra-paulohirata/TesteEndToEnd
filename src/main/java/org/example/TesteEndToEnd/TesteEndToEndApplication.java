package org.example.TesteEndToEnd;

import org.example.TesteEndToEnd.model.Usuario;
import org.example.TesteEndToEnd.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.stream.LongStream;

@SpringBootApplication
public class TesteEndToEndApplication {

	public static void main(String[] args) {
		SpringApplication.run(TesteEndToEndApplication.class, args);
	}

	@Bean
	CommandLineRunner init(UsuarioRepository repository) {
		return args -> {
			repository.deleteAll();
			LongStream.range(1, 11)
					.mapToObj(i -> {
						Usuario c = new Usuario();
						c.setNome("Usuario " + i);
						c.setLogradouro("Rua Vergueiro" + i);
						c.setNumero(i + "" + i);
						c.setCep("00000-000");
						c.setCidade("Sao Paulo");
						c.setEstado("SP");
						return c;
					})
					.map(v -> repository.save(v))
					.forEach(System.out::println);
		};
	}
}
