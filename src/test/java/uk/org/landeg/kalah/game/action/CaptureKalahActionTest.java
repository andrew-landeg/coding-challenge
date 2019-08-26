package uk.org.landeg.kalah.game.action;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.test.context.junit4.SpringRunner;

import uk.org.landeg.kalah.CommonTestConfiguration;
import uk.org.landeg.kalah.components.KalahGameState;
import uk.org.landeg.kalah.game.KalahGameEngine;

@RunWith(SpringRunner.class)
@Import(CommonTestConfiguration.class)
public class CaptureKalahActionTest {
	@TestConfiguration
	static class Config {
		@Bean
		KalahAction captureAction() {
			return new CaptureKalahAction();
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
	public void assertRuleAppliesWhenExpected() {
		final KalahGameState game = new KalahGameState();
		service.initialiseGame(game, 3);
		game.setRecentPit(4);
		game.getPits().put(1, 3);
		game.getPits().put(10, 2);
		game.getPits().put(4, 1);
		Assert.assertTrue(action.applies(game));
		action.processAction(game);
		Assert.assertEquals(0, game.getPits().get(4).intValue());
		Assert.assertEquals(0, game.getPits().get(10).intValue());
		Assert.assertEquals(3, game.getPits().get(7).intValue());
	}

	/**
	 * identical scenario to above, but last pit was not empty 
	 */
	@Test
	public void assertRuleRejectedWhenExpected() {
		final KalahGameState game = new KalahGameState();
		service.initialiseGame(game, 3);
		game.setRecentPit(4);
		game.getPits().put(1, 3);
		game.getPits().put(10, 2);
		game.getPits().put(4, 2);
		Assert.assertFalse(action.applies(game));
	}
}
