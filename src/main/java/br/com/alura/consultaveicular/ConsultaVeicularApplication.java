package br.com.alura.consultaveicular;


import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import br.com.alura.consultaveicular.principal.Principal;

@SpringBootApplication
public class ConsultaVeicularApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(ConsultaVeicularApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Principal principal = new Principal();
		principal.exibeMenu();
	}
}
