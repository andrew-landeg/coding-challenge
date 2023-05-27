package uk.org.landeg.kalah.api.transformer;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import uk.org.landeg.kalah.api.model.PerformMoveResponseModel;
import uk.org.landeg.kalah.components.KalahGameState;
import uk.org.landeg.kalah.game.KalahGameEngine;
import uk.org.landeg.kalah.game.action.KalahAction;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PerformMoveTransformerTest {
	private PerformMoveResponseTransformer transformer = new PerformMoveResponseTransformer();

	// provide an action to prevent gameservice load from failing.
	@MockBean
	KalahAction action;

	@Autowired
	KalahGameEngine gameService;

	@Test
	void assertTransformSuccessful() {
		final KalahGameState game = new KalahGameState();
		game.setUrl("http://dont-care-where/games/123456");
		game.setGameId(System.currentTimeMillis());
		for (int pitId = 1 ; pitId <= 14 ; pitId++) {
			game.getPits().put(pitId, pitId);
		}
		final PerformMoveResponseModel response = transformer.toRest(game);
		assertEquals(response.getId(), Long.toString(game.getGameId()));
		assertThat(response.getUrl()).isEqualTo(game.getUrl());
		assertThat(14).isEqualTo(response.getStatus().size());
		for (int pitId = 1 ; pitId <= 14 ; pitId++) {
			final String pitIdString = Integer.toString(pitId);
			assertTrue(response.getStatus().containsKey(pitIdString));
			assertEquals(pitIdString, response.getStatus().get(pitIdString));
		}
		
	}
}
