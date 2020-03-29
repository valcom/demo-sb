package com.example.demo;

import static org.junit.Assert.assertThat;

import java.util.Arrays;

import javax.annotation.PostConstruct;

import org.json.JSONException;
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
	@Value("${url.base}")
	private String baseUrl;
	
	@LocalServerPort
	private int port;
	
	private String url;
	
	@PostConstruct
	public void init() {
		url=baseUrl+":"+port;
		//UriComponentsBuilder.fromHttpUrl("{url.base}:${local.server.port}/demo/persona").build().encode().toUri();
	    restTemplate.getRestTemplate().setRequestFactory(new BufferingClientHttpRequestFactory(new SimpleClientHttpRequestFactory()));
		restTemplate.getRestTemplate().getInterceptors().add(new RequestResponseLoggingInterceptor());
	}
	

	@Autowired
	private TestRestTemplate restTemplate;

	@Test
	public void testJSON() throws JSONException {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		HttpEntity<String> entity = new HttpEntity<String>(headers);
		ResponseEntity<String> response = restTemplate.exchange(url+"/demo/persona",HttpMethod.GET,entity,String.class);
		String expected = "{nome:pippo,cognome:baudo,cf:XXXXXXXXXXXXX}";
		JSONAssert.assertEquals(expected, response.getBody(), false);
	}
	@Test
	public void testXML() {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_XML));
		HttpEntity<String> entity = new HttpEntity<String>(headers);
		ResponseEntity<String> response = restTemplate.exchange(url+"/demo/persona",HttpMethod.GET, entity, String.class);
		String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><persona><cf>XXXXXXXXXXXXX</cf><cognome>baudo</cognome><nome>pippo</nome></persona>";
		assertThat(expected, CompareMatcher.isIdenticalTo(response.getBody()));
	}
	
	@Test
	public void testObject() {
		PersonaModel response = restTemplate.getForObject(url+"/demo/persona", PersonaModel.class);
		PersonaModel expected = new PersonaModel();
		expected.setNome("pippo");
		expected.setCognome("baudo");
		expected.setCf("XXXXXXXXXXXXX");	
		assertThat(expected, CompareMatcher.isIdenticalTo(response));
	}


}
