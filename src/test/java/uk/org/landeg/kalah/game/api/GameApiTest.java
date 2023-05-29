package uk.org.landeg.kalah.game.api;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
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
@ExtendWith(MockitoExtension.class)
class GameApiTest {
	@Mock
	KalahService kalahWebService;

	@InjectMocks
	GameApi api;

	@Test
	void assertCreateGameBehavior() {
		int stones = KalahGameBoardStandard.DEFAULT_INIT_STONES;
		final CreateGameResponseModel modelResponse = new CreateGameResponseModel();
		when(kalahWebService.createGame(stones)).
			thenReturn(modelResponse);
		final ResponseEntity<CreateGameResponseModel> response = api.createGame(Optional.empty());
		// assert the service is correctly invoked, and that something sensible is returned.
		verify(kalahWebService, times(1)).createGame(stones);
		assertThat(HttpStatus.CREATED).isEqualTo(response.getStatusCode());
		assertSame(modelResponse, response.getBody());
	}

	@Test
	void assertMakeMoveBehavior() {
		long gameId = 123456;
		int pitId = 1;
		final PerformMoveResponseModel repsonseModel = new PerformMoveResponseModel(); 
		when(kalahWebService.makeMove(Mockito.anyLong(), Mockito.anyInt()))
			.thenReturn(repsonseModel);
		final PerformMoveResponseModel actualResponse = api.makeMove(gameId, pitId);
		verify(kalahWebService, times(1)).makeMove(gameId, pitId);
		assertSame(repsonseModel, actualResponse);
	}
}
