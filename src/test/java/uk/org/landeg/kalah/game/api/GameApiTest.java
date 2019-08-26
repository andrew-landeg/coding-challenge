package uk.org.landeg.kalah.game.api;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import uk.org.landeg.kalah.api.GameApi;
import uk.org.landeg.kalah.api.model.CreateGameResponseModel;
import uk.org.landeg.kalah.api.model.PerformMoveResponseModel;
import uk.org.landeg.kalah.game.KalahGameBoardStandard;
import uk.org.landeg.kalah.service.KalahService;

/**
 * Game API unit tests.
 *
 * @author Andrew Landeg
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class GameApiTest {
	@Mock
	KalahService kalahWebService;

	@InjectMocks
	GameApi api = new GameApi();

	@Test
	public void assertCreateGameBehavior() {
		int stones = KalahGameBoardStandard.DEFAULT_INIT_STONES;
		final CreateGameResponseModel modelResponse = new CreateGameResponseModel();
		when(kalahWebService.createGame(stones)).
			thenReturn(modelResponse);
		final ResponseEntity<CreateGameResponseModel> response = api.createGame(Optional.empty());
		// assert the service is correctly invoked, and that something sensible is returned.
		verify(kalahWebService, times(1)).createGame(stones);
		Assert.assertEquals(HttpStatus.CREATED, response.getStatusCode());
		Assert.assertSame(modelResponse, response.getBody());
	}

	@Test
	public void assertMakeMoveBehavior() {
		long gameId = 123456;
		int pitId = 1;
		final PerformMoveResponseModel repsonseModel = new PerformMoveResponseModel(); 
		when(kalahWebService.makeMove(Mockito.anyLong(), Mockito.anyInt()))
			.thenReturn(repsonseModel);
		final PerformMoveResponseModel actualResponse = api.makeMove(gameId, pitId);
		verify(kalahWebService, times(1)).makeMove(gameId, pitId);
		Assert.assertSame(repsonseModel, actualResponse);
	}
}
