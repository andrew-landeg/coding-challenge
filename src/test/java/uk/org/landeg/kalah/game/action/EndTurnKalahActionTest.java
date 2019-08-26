package uk.org.landeg.kalah.game.action;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Assert;
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

@RunWith(SpringRunner.class)
@Import(CommonTestConfiguration.class)
public class EndTurnKalahActionTest {
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
	public void assertRuleAlwaysApplies() {
		Assert.assertTrue(action.applies(new KalahGameState()));
	}

	@Test
	public void assertOpponentsTurnWhenApplied() {
		KalahGameState game = Mockito.mock(KalahGameState.class);
		when(game.getCurrentPlayer()).thenReturn(Player.SOUTH);
		action.processAction(game);
		verify(game, times(1)).setCurrentPlayer(Player.NORTH);
	}
}
