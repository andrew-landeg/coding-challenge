package uk.org.landeg.kalah.game.api;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import uk.org.landeg.kalah.api.model.CreateGameResponseModel;
import uk.org.landeg.kalah.api.model.PerformMoveResponseModel;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)
/**
 * Test some simple game scenarios end to end.
 *
 * Full stack tests using the RestTemplate to perform requests.
 * 
 * TODO: would be nice to flesh these out with a full game.
 *
 * @author Andrew Landeg
 * 
 */
public class GameApiScenarioTests {
	Logger log = LoggerFactory.getLogger(this.getClass());
	private static final int MIN_PIT = 1;
	private static final int MAX_PIT = 14;

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

	/**
	 * Test the outcome of a single valid move.
	 * 
	 * assert response is 200
	 * assert a response payload is sent
	 * assert game id is present on response
	 * assert game url is present on response
	 * assert game status is present and correct
	 */
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

	/**
	 * Test the outcome of an invalid move.
	 * 
	 * PLayers cannot start a move on an empty pit
	 */
	@Test
	public void assertClientExceptionOnPlayEmptyPit() {
		final String gameId = createGame().getId();
		// south player - move pit 1
		// ... end turn in kalah, extra move this turn...
		Assert.assertEquals(HttpStatus.OK, makeMove(gameId, 1).getStatusCode());
		// pit 1 is now empty - attempt playing it.
		Assert.assertEquals(HttpStatus.BAD_REQUEST, makeMove(gameId, 1).getStatusCode());
	}
	

	/**
	 * Test the outcome of playing an invalid put ID
	 *
	 * Expected - client error.
	 */
	@Test
	public void assertClientExceptionOnInvaidPit() {
		final String gameId = createGame().getId();
		Assert.assertEquals(HttpStatus.BAD_REQUEST, makeMove(gameId, 20).getStatusCode());
	}

	
	@Test
	public void playGameScenario() throws URISyntaxException, IOException {
		log.debug("Starting game scenario");
		final ResponseEntity<CreateGameResponseModel> response = 
				restTemplate.postForEntity(getGameApi(), null, CreateGameResponseModel.class);
		final String gameId= response.getBody().getId();
		log.debug("Created game {}", gameId);
		PerformMoveResponseModel moveResponse = null;
		final List<Integer> pitIds = extractMovesFromHarFile();

		for (Integer pitId : pitIds) {
			final String endpoint = getMakeMoveEndpoint(gameId, pitId);
			log.debug("making move with pit {} : {}", pitId, endpoint);
			final HttpEntity<Void> entity = buildHttpEntity();
			try {
				final ResponseEntity<PerformMoveResponseModel> httpResponse = 
						restTemplate.exchange(endpoint, HttpMethod.PUT, entity, PerformMoveResponseModel.class);
				moveResponse = httpResponse.getBody();
			} catch (HttpClientErrorException e) {
				// oops I made some moves out of turn when generating the test set!
				if (e.getStatusCode() == HttpStatus.BAD_REQUEST) {
					log.debug("bad request - playing illegal move for pit {}, ignoring", pitId);
					// ignore it - 400.
				} else {
					throw e;
				}
			}
		}
		final String expectedSouthScore = "40";
		final String expectedNorthScore = "32";
		final String southKalah = "7";
		final String northKalah = "14";
		Assert.assertEquals(expectedSouthScore, moveResponse.getStatus().get(southKalah));
		Assert.assertEquals(expectedNorthScore, moveResponse.getStatus().get(northKalah));
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
	
	private List<Integer> extractMovesFromHarFile() throws IOException {
		InputStream is = this.getClass().getClassLoader().getResourceAsStream("game-scenario.har");
		if (is == null) {
			is = this.getClass().getClassLoader().getResourceAsStream("src/test/resources/game-scenario.har");
		}
		Assert.assertNotNull(is);
		
		Path path = (new File(".").toPath().resolve("/game-scenario.har"));
		if (!path.toFile().exists()) {
			path = new File(".").toPath().resolve("./src/test/resources/game-scenario.har");
		}

		Pattern pattern = Pattern.compile("games\\/\\d+\\/pits\\/(\\d+)");

		return Files.readAllLines(path).stream()
			.filter(line -> line.contains("/pits/"))
			.peek(line -> System.out.println(line))
			.map(line ->  {
				final Matcher matcher = pattern.matcher(line);
				if (matcher.find()) {
					return matcher.group(1);
				}
				return null;
			})
			.filter(Objects::nonNull)
			.map(Integer::parseInt)
			.collect(Collectors.toList());
	}
}
