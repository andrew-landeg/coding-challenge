package uk.org.landeg.kalah.persistence;

import org.junit.jupiter.api.Test;

import uk.org.landeg.kalah.Constants.Player;
import uk.org.landeg.kalah.components.KalahGameState;
import uk.org.landeg.kalah.persistence.domain.GameStateJpa;
import uk.org.landeg.kalah.persistence.domain.PitStateJpa;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GameStateTransformerTest {
	final GameStateJpaTransformer transformer = new GameStateJpaTransformer();

	// test fixtures...
	private final Integer expectedRecentPit = 3;
	private final boolean expectedInProgress = true;
	private final Player expectedPlayer = Player.SOUTH;
	private final String expectedUrl = "http://dont-care-where.com/games/9871298372"; 
	private final Long expectedId = 123456L;
	private final int expectedPitId = 1;
	private final int expectedStoneCount = 3;
	

	@Test
	void assertToJpaSuccessful() {
		final KalahGameState business = new KalahGameState();
		business.setCurrentPlayer(expectedPlayer);
		business.setInProgress(expectedInProgress);
		business.setRecentPit(expectedRecentPit);
		business.setUrl(expectedUrl);
		business.setGameId(expectedId);

		business.getPits().put(expectedPitId, expectedStoneCount);
		
		final GameStateJpa jpa = transformer.toJpa(business, null);

		assertThat(jpa).isNotNull();
		assertThat(expectedUrl).isEqualTo(jpa.getUrl());
		assertThat(expectedInProgress).isEqualTo(jpa.getInProgress());
		assertThat(expectedPlayer).isEqualTo(jpa.getCurrentPlayer());
		assertThat(expectedRecentPit).isEqualTo(jpa.getRecentPit());
		assertThat(expectedId).isEqualTo(jpa.getGameId());
		
		assertThat(1).isEqualTo(jpa.getPits().size());
		assertThat(expectedStoneCount).isEqualTo(jpa.getPits().get(expectedPitId).getStones().intValue());
	}


	@Test
	void assertFromJpaSuccessful() {
		final GameStateJpa jpa = new GameStateJpa();
		jpa.setCurrentPlayer(expectedPlayer);
		jpa.setInProgress(expectedInProgress);
		jpa.setRecentPit(expectedRecentPit);
		jpa.setUrl(expectedUrl);
		jpa.setGameId(expectedId);
		
		final PitStateJpa pit = new PitStateJpa(jpa, expectedPitId, expectedStoneCount);
		jpa.getPits().put(expectedPitId, pit);
		
		final KalahGameState gameState = transformer.fromJpa(jpa, null);
		assertThat(gameState).isNotNull();
		assertThat(expectedUrl).isEqualTo(gameState.getUrl());
		assertThat(expectedInProgress).isEqualTo(gameState.isInProgress());
		assertThat(expectedPlayer).isEqualTo(gameState.getCurrentPlayer());
		assertThat(expectedRecentPit).isEqualTo(gameState.getRecentPit());
		assertThat(expectedId).isEqualTo(gameState.getGameId() );
		assertThat(1).isEqualTo(gameState.getPits().size());
		assertTrue(gameState.getPits().containsKey(expectedPitId));
		assertThat(expectedStoneCount).isEqualTo(gameState.getPits().get(expectedPitId).intValue());
	}
}
