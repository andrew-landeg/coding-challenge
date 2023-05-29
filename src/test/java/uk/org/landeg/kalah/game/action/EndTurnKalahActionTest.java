package uk.org.landeg.kalah.game.action;

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

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@Import(CommonTestConfiguration.class)
class EndTurnKalahActionTest {
	@TestConfiguration
	static class Config {
		@Bean
		KalahAction endTurnAction() {
			return new EndTurnKalahAction();
		}
	}

	@Autowired
	KalahAction action;

	@Test
	void assertRuleAlwaysApplies() {
		assertTrue(action.applies(new KalahGameState()));
	}

	@Test
	void assertOpponentsTurnWhenApplied() {
		KalahGameState game = Mockito.mock(KalahGameState.class);
		when(game.getCurrentPlayer()).thenReturn(Player.SOUTH);
		action.processAction(game);
		verify(game, times(1)).setCurrentPlayer(Player.NORTH);
	}
}
