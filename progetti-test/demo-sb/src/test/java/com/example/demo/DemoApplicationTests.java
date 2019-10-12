package com.example.demo;

import static org.junit.Assert.assertThat;

import java.util.Arrays;

import org.json.JSONException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.xmlunit.matchers.CompareMatcher;

@RunWith(SpringRunner.class)
@SpringBootTest(classes=DemoApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class DemoApplicationTests {
	@LocalServerPort
	private int port;

	TestRestTemplate restTemplate = new TestRestTemplate();

	@Test
	public void testJSON() throws JSONException {
		HttpHeaders headers = new HttpHeaders();

		HttpEntity<String> entity = new HttpEntity<String>(null, headers);

		ResponseEntity<String> response = restTemplate.exchange(
				createURLWithPort("/demo/persona"),
				HttpMethod.GET, entity, String.class);

		String expected = "{nome:pippo,cognome:baudo,cf:XXXXXXXXXXXXX}";

		JSONAssert.assertEquals(expected, response.getBody(), false);
	}
	@Test
	public void testXML() {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_XML));
		HttpEntity<String> entity = new HttpEntity<String>(null, headers);

		ResponseEntity<String> response = restTemplate.exchange(
				createURLWithPort("/demo/persona"),
				HttpMethod.GET, entity, String.class);

		String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><persona><cf>XXXXXXXXXXXXX</cf><cognome>baudo</cognome><nome>pippo</nome></persona>";
		assertThat(expected, CompareMatcher.isIdenticalTo(response.getBody()));
		
	}
	
	private String createURLWithPort(String uri) {
		return "http://localhost:" + port + uri;
	}

}
