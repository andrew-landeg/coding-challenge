package uk.org.landeg.kalah.game.action;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import uk.org.landeg.kalah.CommonTestConfiguration;
import uk.org.landeg.kalah.components.KalahGameState;
import uk.org.landeg.kalah.game.KalahGameBoard;
import uk.org.landeg.kalah.game.KalahGameEngine;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@Import(CommonTestConfiguration.class)
class CaptureKalahActionTest {
	@TestConfiguration
	static class Config {
		@Bean
		@Autowired
		KalahAction captureAction(KalahGameBoard gameBoard) {
			return new CaptureKalahAction(gameBoard);
		}
	}

	@Autowired
	KalahGameEngine service;

	@Autowired
	KalahAction action;

	/**
	 * scenario - move from square 1 (3 stones) -> 4 (empty)
	 * square 10 has 2 stones.
	 * expected capture 1 + 2 stones.
	 */
	@Test
	void assertRuleAppliesWhenExpected() {
		final KalahGameState game = new KalahGameState();
		service.initialiseGame(game, 3);
		game.setRecentPit(4);
		game.getPits().put(1, 3);
		game.getPits().put(10, 2);
		game.getPits().put(4, 1);
		assertTrue(action.applies(game));
		action.processAction(game);
		assertThat(0).isEqualTo(game.getPits().get(4).intValue());
		assertThat(0).isEqualTo(game.getPits().get(10).intValue());
		assertThat(3).isEqualTo(game.getPits().get(7).intValue());
	}

	/**
	 * identical scenario to above, but last pit was not empty 
	 */
	@Test
	void assertRuleRejectedWhenExpected() {
		final KalahGameState game = new KalahGameState();
		service.initialiseGame(game, 3);
		game.setRecentPit(4);
		game.getPits().put(1, 3);
		game.getPits().put(10, 2);
		game.getPits().put(4, 2);
		assertFalse(action.applies(game));
	}
}
