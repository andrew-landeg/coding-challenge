package uk.org.landeg.kalah.game;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import uk.org.landeg.kalah.CommonTestConfiguration;
import uk.org.landeg.kalah.Constants.Player;
import uk.org.landeg.kalah.components.KalahGameState;
import uk.org.landeg.kalah.exception.KalahClientException;
import uk.org.landeg.kalah.game.action.KalahAction;
import uk.org.landeg.kalah.game.action.KalahMoveProcessor;

@RunWith(SpringRunner.class)
@Import(CommonTestConfiguration.class)
public class KalahGameEngineTest {
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
	public void assertGameInitAsExpected() {
		final KalahGameState game = new KalahGameState();
		gameService.initialiseGame(game);
		game.getPits().entrySet().forEach(pitEntry -> {
			int expectedValue = 
					gameBoard.isKalah(pitEntry.getKey()) ? 0 : KalahGameBoardStandard.DEFAULT_INIT_STONES;
			int actualValue = pitEntry.getValue();
			Assert.assertEquals(expectedValue, actualValue);
		});
		Assert.assertEquals(0, game.getPits().get(gameBoard.getPlayerKalah().get(Player.NORTH)).intValue());
		Assert.assertEquals(0, game.getPits().get(gameBoard.getPlayerKalah().get(Player.SOUTH)).intValue());
		Assert.assertTrue(game.isInProgress());
		Assert.assertEquals(Player.SOUTH, game.getCurrentPlayer());
	}

	@Test
	public void assertProcessActionInvokedWhenApplicable() {
		final KalahGameState game = new KalahGameState();
		when(mockAction.applies(game)).thenReturn(true);
		gameService.initialiseGame(game);
		gameService.performMove(game, 1);
		verify(mockAction, times(1)).applies(game);
		verify(mockAction, times(1)).processAction(game);
	}

	@Test
	public void assertProcessActionNotInvokedWhenNotApplicable() {
		final KalahGameState game = new KalahGameState();
		when(mockAction.applies(game)).thenReturn(false);
		gameService.initialiseGame(game);
		gameService.performMove(game, 1);
		verify(mockAction, times(1)).applies(game);
		verify(mockAction, never()).processAction(game);
	}

	@Test
	public void assertProcessMoveInvokedWhenAllowed() {
		final KalahGameState game = new KalahGameState();
		gameService.initialiseGame(game);
		gameService.performMove(game, 1);
		verify(mockMoveProcessor, times(1)).processMove(game, 1);
	}

	@Test
	public void assertSouthPlayerInferredWhenNoPlayerSet() {
		final KalahGameState game = new KalahGameState();
		gameService.initialiseGame(game);
		game.setCurrentPlayer(null);
		gameService.performMove(game, 1);
		// stays south because we're not using the processors to change to north at turn end.
		Assert.assertEquals(Player.SOUTH, game.getCurrentPlayer());
	}

	@Test(expected=KalahClientException.class)
	public void assertClientExceptionOnGameEnded() {
		final KalahGameState game = new KalahGameState();
		gameService.initialiseGame(game);
		game.setInProgress(false);
		gameService.performMove(game, 1);
	}

	@Test(expected=KalahClientException.class)
	public void assertClientExceptionOnOpponentsPit() {
		final KalahGameState game = new KalahGameState();
		gameService.initialiseGame(game);
		gameService.performMove(game, gameBoard.getPlayerPits().get(Player.NORTH).get(0));
	}

	@Test(expected=KalahClientException.class)
	public void assertClientExceptionOnEmptyPitMove() {
		final KalahGameState game = new KalahGameState();
		gameService.initialiseGame(game);
		game.getPits().take(1);
		gameService.performMove(game, 1);
	}
}
