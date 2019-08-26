package uk.org.landeg.kalah.game.api;

import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import uk.org.landeg.kalah.api.model.CreateGameResponseModel;
import uk.org.landeg.kalah.api.model.PerformMoveResponseModel;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
/**
 * Test some simple game scenarios end to end.
 *
 * TODO: would be nice to flesh these out with a full game.
 *
 * @author Andrew Landeg
 * 
 */
public class GameApiScenarioTests {
	private static final int MIN_PIT = 1;
	private static final int MAX_PIT = 14;

	@Autowired
	MockMvc mvc;
	
	@LocalServerPort
	Integer serverPort;

	String gameApiUrl;

	private RestTemplate restTemplate = new RestTemplate();

	@Test
	public void contextLoads() {
	}

	/**
	 * Test the create game scenario.
	 */
	@Test
	public void assertCreateGameResponse() {
		final ResponseEntity<CreateGameResponseModel> response = 
				restTemplate.postForEntity(getGameApi(), null, CreateGameResponseModel.class);
		// COA - response status is 201 (Created)
		Assert.assertEquals(HttpStatus.CREATED, response.getStatusCode());
		final CreateGameResponseModel responseModel = response.getBody();
		Assert.assertNotNull(responseModel);
		String regex = (getGameApi() + "/\\d+").replace("/", "\\/");
		Assert.assertTrue(responseModel.getUrl().matches(regex));
	}

	@Test
	public void assertSingleValidMoveScenario() {
		final String gameId = createGame().getId();
		final String endpoint = getMakeMoveEndpoint(gameId, 1);
		
		final HttpEntity<Void> entity = buildHttpEntity();

		final ResponseEntity<PerformMoveResponseModel> response = 
				restTemplate.exchange(endpoint, HttpMethod.PUT, entity, PerformMoveResponseModel.class);

		// COA : assert response is 200
		Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
		final PerformMoveResponseModel model = response.getBody();
		// CAO : assert a response payload is sent
		Assert.assertNotNull(model);
		// COA : assert game id is present on response
		Assert.assertNotNull(model.getId());
		Assert.assertEquals(gameId, model.getId());

		// COA : assert game url is present on response
		final String expectedEndpoint = getGameApi() + "/" + gameId;
		Assert.assertNotNull(model.getUrl());
		Assert.assertEquals(expectedEndpoint, model.getUrl());
		
		// COA : assert game status is present and correct
		// (note key and value are both specified as strings)
		final Map<String, String> status = model.getStatus();
		Assert.assertNotNull(status);
		for (int pitId = MIN_PIT ; pitId <= MAX_PIT ; pitId++) {
			Assert.assertTrue(status.containsKey(Integer.toString(pitId)));
		}
		assertStoneCountInPits(status, 0,7,7,7,7,7,1,6,6,6,6,6,6,0);
	}

	@Test
	public void assertClientExceptionOnPlayEmptyPit() {
		final String gameId = createGame().getId();
		// south player - move pit 1
		// ... end turn in kalah, extra move this turn...
		Assert.assertEquals(HttpStatus.OK, makeMove(gameId, 1).getStatusCode());
		// pit 1 is now empty - attempt playing it.
		Assert.assertEquals(HttpStatus.BAD_REQUEST, makeMove(gameId, 1).getStatusCode());
	}
	

	@Test
	public void assertClientExceptionOnInvaidPit() {
		final String gameId = createGame().getId();
		Assert.assertEquals(HttpStatus.BAD_REQUEST, makeMove(gameId, 20).getStatusCode());
	}

	private void assertStoneCountInPits(Map<String, String> pits, Integer... stones) {
		for (int idx = 0 ; idx  < stones.length ; idx++) {
			final String expected = Integer.toString(stones[idx]);
			final String pitIdStr = Integer.toString(idx + 1);
			Assert.assertEquals(expected, pits.get(pitIdStr));
		}
	}

	private CreateGameResponseModel createGame() {
		final ResponseEntity<CreateGameResponseModel> response = 
				restTemplate.postForEntity(getGameApi(), null, CreateGameResponseModel.class);
		return response.getBody();
	}

	private ResponseEntity<PerformMoveResponseModel> makeMove(final String gameId, final Integer pitId) {
		final String endpoint = getMakeMoveEndpoint(gameId, pitId);
		final HttpEntity<Void> entity = buildHttpEntity();
		try {
			final ResponseEntity<PerformMoveResponseModel> response = 
					restTemplate.exchange(endpoint, HttpMethod.PUT, entity, PerformMoveResponseModel.class);
			return response;
		} catch (HttpClientErrorException | HttpServerErrorException ex) {
			// bit of a workaround to save heaps of error handling in client code...
			return ResponseEntity.status(ex.getStatusCode()).body(null);
		}
	}

	private HttpEntity<Void> buildHttpEntity() {
		final HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		return new HttpEntity<Void>(headers);
	}

	private String getGameApi() {
		if (gameApiUrl == null) {
			gameApiUrl = String.format("http://localhost:%d/games", serverPort);
		}
		return gameApiUrl;
	}

	private String getMakeMoveEndpoint(final String gameId, final Integer pitId) {
		return String.format("%s/%s/pits/%d", getGameApi(), gameId, pitId);
	}
}
