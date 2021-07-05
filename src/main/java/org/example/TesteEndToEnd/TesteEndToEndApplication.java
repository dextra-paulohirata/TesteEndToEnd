package org.example.TesteEndToEnd;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TesteEndToEndApplication {

	public static void main(String[] args) {
		SpringApplication.run(TesteEndToEndApplication.class, args);
	}

//	@Bean
//	CommandLineRunner init(UsuarioRepository repository) {
//		return args -> {
//			repository.deleteAll();
//			LongStream.range(1, 11)
//					.mapToObj(i -> {
//						Usuario c = new Usuario();
//						c.setNome("Usuario " + i);
//						c.setLogradouro("Rua Vergueiro" + i);
//						c.setNumero(i + "" + i);
//						c.setCep("00000-000");
//						c.setCidade("Sao Paulo");
//						c.setEstado("SP");
//						return c;
//					})
//					.map(v -> repository.save(v))
//					.forEach(System.out::println);
//		};
//	}
}
