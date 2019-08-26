package uk.org.landeg.kalah.game.action;

import static org.hamcrest.CoreMatchers.any;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import uk.org.landeg.kalah.CommonTestConfiguration;
import uk.org.landeg.kalah.Constants.Player;
import uk.org.landeg.kalah.components.KalahGameState;
import uk.org.landeg.kalah.game.KalahGameBoard;
import uk.org.landeg.kalah.game.KalahPitDecorator;

@RunWith(SpringRunner.class)
@Import(CommonTestConfiguration.class)
public class FinishInKalahActionTest {
	@TestConfiguration
	static class Config {
		@Bean
		KalahAction finishInKalahAction() {
			return new FinishInKalahAction();
		}
	}

	@Autowired
	KalahGameBoard gameBoard;

	@Autowired
	KalahAction action;

	@Test
	public void assertApplicable() {
		KalahGameState game = Mockito.mock(KalahGameState.class);
		KalahPitDecorator pits = Mockito.mock(KalahPitDecorator.class);
		Player player = Player.SOUTH;
		final int kalahSouth = gameBoard.getPlayerKalah().get(player);
		when(game.getRecentPit()).thenReturn(kalahSouth);
		when(game.getPits()).thenReturn(pits);
		when(game.getCurrentPlayer()).thenReturn(player);
		when(pits.get(kalahSouth)).thenReturn(1);
		assertTrue(action.applies(game));
	}

	@Test
	public void assertNoPlayerChangeOnProcess() {
		KalahGameState game = Mockito.mock(KalahGameState.class);
		Player player = Player.SOUTH;
		when(game.getCurrentPlayer()).thenReturn(player);
		verify(game, never()).setCurrentPlayer(Player.NORTH);
	}
}
