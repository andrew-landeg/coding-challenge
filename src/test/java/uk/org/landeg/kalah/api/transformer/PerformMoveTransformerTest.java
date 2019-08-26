package uk.org.landeg.kalah.api.transformer;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import uk.org.landeg.kalah.api.model.PerformMoveResponseModel;
import uk.org.landeg.kalah.components.KalahGameState;
import uk.org.landeg.kalah.game.KalahGameEngine;
import uk.org.landeg.kalah.game.action.KalahAction;

public class PerformMoveTransformerTest {
	private PerformMoveResponseTransformer transformer = new PerformMoveResponseTransformer();

	// provide an action to prevent gameservice load from failing.
	@MockBean
	KalahAction action;

	@Autowired
	KalahGameEngine gameService;

	@Test
	public void assertTransformSuccessful() {
		final KalahGameState game = new KalahGameState();
		game.setUrl("http://dont-care-where/games/123456");
		game.setGameId(System.currentTimeMillis());
		for (int pitId = 1 ; pitId <= 14 ; pitId++) {
			game.getPits().put(pitId, pitId);
		}
		final PerformMoveResponseModel response = transformer.toRest(game);
		Assert.assertEquals(response.getId(), Long.toString(game.getGameId()));
		Assert.assertEquals(response.getUrl(), game.getUrl());
		Assert.assertEquals(14, response.getStatus().size());
		for (int pitId = 1 ; pitId <= 14 ; pitId++) {
			final String pitIdString = Integer.toString(pitId);
			Assert.assertTrue(response.getStatus().containsKey(pitIdString));
			Assert.assertEquals(pitIdString, response.getStatus().get(pitIdString));
		}
		
	}
}
