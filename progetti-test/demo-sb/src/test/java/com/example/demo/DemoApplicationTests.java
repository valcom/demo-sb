package com.example.demo;

import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

import org.json.JSONException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.util.UriComponentsBuilder;
import org.xmlunit.matchers.CompareMatcher;

import com.example.demo.model.PersonaModel;

@RunWith(SpringRunner.class)
@SpringBootTest(classes=DemoApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations="classpath:application-test.properties")
public class DemoApplicationTests {
	@Value("${endpoint}")
	private String endpoint;
	
	@LocalServerPort
	private int port;
	
	private URI uri;
	
	@Autowired
	private TestRestTemplate restTemplate;
	
	@Before
	public void init() throws MalformedURLException {
		uri = UriComponentsBuilder.fromHttpUrl(endpoint+":"+port+"/demo/persona").build().encode().toUri();
	    restTemplate.getRestTemplate().setRequestFactory(new BufferingClientHttpRequestFactory(new SimpleClientHttpRequestFactory()));
		restTemplate.getRestTemplate().getInterceptors().add(new RequestResponseLoggingInterceptor());
	}

	@Test
	public void testJSON() throws IOException, URISyntaxException, JSONException  {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		HttpEntity<String> entity = new HttpEntity<String>(headers);
		ResponseEntity<String> response = restTemplate.exchange(uri,HttpMethod.GET,entity,String.class);
		String expected = new String(Files.readAllBytes(Paths.get(this.getClass().getClassLoader().getResource("persona.json").toURI())));
		JSONAssert.assertEquals(expected, response.getBody(), false);
	}
	@Test
	public void testXML() throws IOException, URISyntaxException  {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_XML));
		HttpEntity<String> entity = new HttpEntity<String>(headers);
		ResponseEntity<String> response = restTemplate.exchange(uri,HttpMethod.GET, entity, String.class);
		String expected = new String(Files.readAllBytes(Paths.get(getClass().getClassLoader().getResource("persona.xml").toURI())));
		assertThat(expected, CompareMatcher.isIdenticalTo(response.getBody()).normalizeWhitespace());
	}
	
	@Test
	public void testObject() {
		PersonaModel response = restTemplate.getForObject(uri, PersonaModel.class);
		PersonaModel expected = new PersonaModel();
		expected.setNome("pippo");
		expected.setCognome("baudo");
		expected.setCf("XXXXXXXXXXXXX");	
		assertThat(expected, CompareMatcher.isIdenticalTo(response));
	}


}
