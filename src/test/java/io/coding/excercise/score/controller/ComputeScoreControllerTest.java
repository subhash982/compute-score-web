package io.coding.excercise.score.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.nio.file.Paths;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.util.LinkedMultiValueMap;

import io.coding.excercise.ComputeScoreWebApplication;
import io.coding.excercise.score.modal.ComputeScoreResponse;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, classes = { ComputeScoreWebApplication.class })
@ActiveProfiles({"test"})
public class ComputeScoreControllerTest {
	
	@LocalServerPort
	int localPort;
	
	@Autowired
	private TestRestTemplate testRestTemplate;
	
	private String filepath = "testdata";

	@Test
	@DisplayName("Test the score API")
	void testScoreApiSuccess() {
		LinkedMultiValueMap<String, Object> parameters = new LinkedMultiValueMap<String, Object>();
		parameters.add("file", new FileSystemResource(Paths.get(filepath + File.separator + "LargeDataSetFile.txt")));
		parameters.add("algorithm", "scoreByFirstName");

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.MULTIPART_FORM_DATA);

		HttpEntity<LinkedMultiValueMap<String, Object>> entity = new HttpEntity<LinkedMultiValueMap<String, Object>>(
				parameters, headers);

		ResponseEntity<ComputeScoreResponse> response = testRestTemplate.exchange(
				"http://localhost:" + localPort + "/api/score", HttpMethod.POST, entity, ComputeScoreResponse.class, "");
		
		assertEquals(response.getStatusCode(), HttpStatus.OK);
		assertEquals(response.getBody().getScore(), 1073712919L);

	}
	
	@Test
	@DisplayName("Test the score API with invalid file")
	void testScoreApiFailure() {
		LinkedMultiValueMap<String, Object> parameters = new LinkedMultiValueMap<String, Object>();
		parameters.add("file", new FileSystemResource(Paths.get(filepath + File.separator + "invalid")));
		parameters.add("algorithm", "scoreByFirstName");

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.MULTIPART_FORM_DATA);

		HttpEntity<LinkedMultiValueMap<String, Object>> entity = new HttpEntity<LinkedMultiValueMap<String, Object>>(
				parameters, headers);

		ResponseEntity<ComputeScoreResponse> response = testRestTemplate.exchange(
				"http://localhost:" + localPort + "/api/score", HttpMethod.POST, entity, ComputeScoreResponse.class, "");
		
		assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);

	}

}
