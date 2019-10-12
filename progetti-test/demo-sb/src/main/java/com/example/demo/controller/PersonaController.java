/**
 * 
 */
package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.PersonaModel;

/**
 * @author valer
 *
 */
@RestController
@RequestMapping(path = "/demo")
public class PersonaController {

	@Autowired
	private PersonaModel persona;

	@RequestMapping(path = "/persona",method = RequestMethod.GET,produces = {MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE})
	public ResponseEntity<PersonaModel> getPersona(){
		ResponseEntity<PersonaModel> res = ResponseEntity.ok(persona); 
		return res;


	}

}
