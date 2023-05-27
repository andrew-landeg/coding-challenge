package uk.org.landeg.kalah.game.action;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import uk.org.landeg.kalah.CommonTestConfiguration;
import uk.org.landeg.kalah.Constants.Player;
import uk.org.landeg.kalah.components.KalahGameState;
import uk.org.landeg.kalah.game.KalahGameBoard;
import uk.org.landeg.kalah.game.KalahPitDecorator;

@ExtendWith(SpringExtension.class)
@Import(CommonTestConfiguration.class)
class FinishInKalahActionTest {
	@TestConfiguration
	static class Config {
		@Bean
		@Autowired
		KalahAction finishInKalahAction(KalahGameBoard gameBoard) {
			return new FinishInKalahAction(gameBoard);
		}
	}

	@Autowired
	KalahGameBoard gameBoard;

	@Autowired
	KalahAction action;

	@Test
	void assertApplicable() {
		KalahGameState game = Mockito.mock(KalahGameState.class);
		KalahPitDecorator pits = Mockito.mock(KalahPitDecorator.class);
		Player player = Player.SOUTH;
		final int kalahSouth = gameBoard.getPlayerKalah().get(player);
		when(game.getRecentPit()).thenReturn(kalahSouth);
		when(game.getPits()).thenReturn(pits);
		when(game.getCurrentPlayer()).thenReturn(player);
		when(pits.get(kalahSouth)).thenReturn(1);
		assertThat(action.applies(game)).isTrue();
	}

	@Test
	void assertNoPlayerChangeOnProcess() {
		KalahGameState game = Mockito.mock(KalahGameState.class);
		Player player = Player.SOUTH;
		when(game.getCurrentPlayer()).thenReturn(player);
		verify(game, never()).setCurrentPlayer(Player.NORTH);
	}
}
