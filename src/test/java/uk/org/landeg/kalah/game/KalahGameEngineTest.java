package uk.org.landeg.kalah.game;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import uk.org.landeg.kalah.CommonTestConfiguration;
import uk.org.landeg.kalah.Constants.Player;
import uk.org.landeg.kalah.components.KalahGameState;
import uk.org.landeg.kalah.exception.KalahClientException;
import uk.org.landeg.kalah.game.action.KalahAction;
import uk.org.landeg.kalah.game.action.KalahMoveProcessor;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@Import(CommonTestConfiguration.class)
class KalahGameEngineTest {
	@Autowired
	KalahGameBoard gameBoard;
	
	@Autowired
	KalahGameEngine gameService;

	@MockBean
	KalahAction mockAction;

	@MockBean
	KalahMoveProcessor mockMoveProcessor;
	
	/**
	 * Game initialisation check.
	 * 
	 * All pits should have intial stones, kalah should have zero.
	 */
	@Test
	void assertGameInitAsExpected() {
		final KalahGameState game = new KalahGameState();
		gameService.initialiseGame(game);
		game.getPits().entrySet().forEach(pitEntry -> {
			int expectedValue = 
					gameBoard.isKalah(pitEntry.getKey()) ? 0 : KalahGameBoardStandard.DEFAULT_INIT_STONES;
			int actualValue = pitEntry.getValue();
			assertEquals(expectedValue, actualValue);
		});
		assertThat(0).isEqualTo(game.getPits().get(gameBoard.getPlayerKalah().get(Player.NORTH)).intValue());
		assertThat(0).isEqualTo(game.getPits().get(gameBoard.getPlayerKalah().get(Player.SOUTH)).intValue());
		assertTrue(game.isInProgress());
		assertThat(Player.SOUTH).isEqualTo(game.getCurrentPlayer());
	}

	@Test
	void assertProcessActionInvokedWhenApplicable() {
		final KalahGameState game = new KalahGameState();
		when(mockAction.applies(game)).thenReturn(true);
		gameService.initialiseGame(game);
		gameService.performMove(game, 1);
		verify(mockAction, times(1)).applies(game);
		verify(mockAction, times(1)).processAction(game);
	}

	@Test
	void assertProcessActionNotInvokedWhenNotApplicable() {
		final KalahGameState game = new KalahGameState();
		when(mockAction.applies(game)).thenReturn(false);
		gameService.initialiseGame(game);
		gameService.performMove(game, 1);
		verify(mockAction, times(1)).applies(game);
		verify(mockAction, never()).processAction(game);
	}

	@Test
	void assertProcessMoveInvokedWhenAllowed() {
		final KalahGameState game = new KalahGameState();
		gameService.initialiseGame(game);
		gameService.performMove(game, 1);
		verify(mockMoveProcessor, times(1)).processMove(game, 1);
	}

	@Test
	void assertSouthPlayerInferredWhenNoPlayerSet() {
		final KalahGameState game = new KalahGameState();
		gameService.initialiseGame(game);
		game.setCurrentPlayer(null);
		gameService.performMove(game, 1);
		// stays south because we're not using the processors to change to north at turn end.
		assertThat(Player.SOUTH).isEqualTo(game.getCurrentPlayer());
	}

	@Test
	void assertClientExceptionOnGameEnded() {
		final KalahGameState game = new KalahGameState();
		gameService.initialiseGame(game);
		game.setInProgress(false);
		assertThrows(KalahClientException.class, () -> gameService.performMove(game, 1));
	}

	@Test
	void assertClientExceptionOnOpponentsPit() {
		final KalahGameState game = new KalahGameState();
		gameService.initialiseGame(game);
		var pitForMove = gameBoard.getPlayerPits().get(Player.NORTH).get(0);
		assertThrows(KalahClientException.class,
				() -> gameService.performMove(game, pitForMove));
	}

	@Test
	void assertClientExceptionOnEmptyPitMove() {
		final KalahGameState game = new KalahGameState();
		gameService.initialiseGame(game);
		game.getPits().take(1);
		assertThrows(KalahClientException.class, () -> gameService.performMove(game, 1));
	}
}
