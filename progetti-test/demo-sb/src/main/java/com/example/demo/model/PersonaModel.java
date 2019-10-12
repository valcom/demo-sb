package com.example.demo.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="persona") 
public class PersonaModel {
	private String nome;
	private String cognome;
	private String cf;
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getCognome() {
		return cognome;
	}
	public void setCognome(String cognome) {
		this.cognome = cognome;
	}
	public String getCf() {
		return cf;
	}
	public void setCf(String cf) {
		this.cf = cf;
	}
}
