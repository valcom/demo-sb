package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.example.demo.model.PersonaModel;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}
	
	@Bean
	public PersonaModel getPersona() {
		PersonaModel persona = new PersonaModel(); 
		persona.setNome("pippo");
		persona.setCognome("baudo");
		persona.setCf("XXXXXXXXXXXXX");
		return persona;
	}

}
