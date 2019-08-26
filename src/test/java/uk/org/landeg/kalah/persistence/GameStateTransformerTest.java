package uk.org.landeg.kalah.persistence;

import org.junit.Assert;
import org.junit.Test;

import uk.org.landeg.kalah.Constants.Player;
import uk.org.landeg.kalah.components.KalahGameState;
import uk.org.landeg.kalah.persistence.domain.GameStateJpa;
import uk.org.landeg.kalah.persistence.domain.PitStateJpa;

public class GameStateTransformerTest {
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
	public void assertToJpaSuccessful() {
		final KalahGameState business = new KalahGameState();
		business.setCurrentPlayer(expectedPlayer);
		business.setInProgress(expectedInProgress);
		business.setRecentPit(expectedRecentPit);
		business.setUrl(expectedUrl);
		business.setGameId(expectedId);

		business.getPits().put(expectedPitId, expectedStoneCount);
		
		final GameStateJpa jpa = transformer.toJpa(business, null);

		Assert.assertNotNull(jpa);
		Assert.assertEquals(expectedUrl, jpa.getUrl());
		Assert.assertEquals(expectedInProgress, jpa.getInProgress());
		Assert.assertEquals(expectedPlayer, jpa.getCurrentPlayer());
		Assert.assertEquals(expectedRecentPit, jpa.getRecentPit());
		Assert.assertEquals(expectedId, jpa.getGameId());
		
		Assert.assertEquals(1, jpa.getPits().size());
		Assert.assertEquals(expectedStoneCount, jpa.getPits().get(expectedPitId).getStones().intValue());
	}


	@Test
	public void assertFromJpaSuccessful() {
		final GameStateJpa jpa = new GameStateJpa();
		jpa.setCurrentPlayer(expectedPlayer);
		jpa.setInProgress(expectedInProgress);
		jpa.setRecentPit(expectedRecentPit);
		jpa.setUrl(expectedUrl);
		jpa.setGameId(expectedId);
		
		final PitStateJpa pit = new PitStateJpa(jpa, expectedPitId, expectedStoneCount);
		jpa.getPits().put(expectedPitId, pit);
		
		final KalahGameState gameState = transformer.fromJpa(jpa, null);
		Assert.assertNotNull(gameState);
		Assert.assertEquals(expectedUrl, gameState.getUrl());
		Assert.assertEquals(expectedInProgress, gameState.isInProgress());
		Assert.assertEquals(expectedPlayer, gameState.getCurrentPlayer());
		Assert.assertEquals(expectedRecentPit, gameState.getRecentPit());
		Assert.assertEquals(expectedId, gameState.getGameId());
		Assert.assertEquals(1, gameState.getPits().size());
		Assert.assertTrue(gameState.getPits().containsKey(expectedPitId));
		Assert.assertEquals(expectedStoneCount, gameState.getPits().get(expectedPitId).intValue());
	}
}
