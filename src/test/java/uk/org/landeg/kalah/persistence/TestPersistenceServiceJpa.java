package uk.org.landeg.kalah.persistence;

import java.util.function.Function;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import uk.org.landeg.kalah.Constants.Player;
import uk.org.landeg.kalah.components.KalahGameState;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestPersistenceServiceJpa {
	@Autowired
	PersistenceService persistenceService;

	final Function<Integer, Integer> stoneCountProducerBase7 = pitId -> pitId % 7;
	final Function<Integer, Integer> stoneCountProducerBase11 = pitId -> pitId % 11;

	@Test
	public void assertRetrievedRecord() {
		final Integer expectedRecentPit = 3;
		final boolean expectedInProgress = true;
		final String expectedUrl = "http://dont-care-where.com/games/9871298372"; 

		KalahGameState game = new KalahGameState();
		game.setCurrentPlayer(Player.SOUTH);
		game.setInProgress(expectedInProgress);
		game.setRecentPit(expectedRecentPit);
		game.setUrl(expectedUrl);
		game.setWinner(Player.SOUTH);
		for (int idx = 1 ; idx <= 14; idx++) game.getPits().put(idx,stoneCountProducerBase7.apply(idx));
		game = persistenceService.save(game);
		
		game = persistenceService.findById(game.getGameId())
				.orElseThrow(() -> new AssertionError("game cannot be retrieved"));
		Assert.assertNotNull(game);
		Assert.assertEquals(expectedRecentPit, game.getRecentPit());
		Assert.assertEquals(Player.SOUTH, game.getCurrentPlayer());
		Assert.assertEquals(Player.SOUTH, game.getWinner());
		Assert.assertEquals(expectedInProgress, game.isInProgress());
		Assert.assertEquals(expectedUrl, game.getUrl());
		for (int pitId = 1; pitId <= 14 ; pitId++) {
			Assert.assertEquals(stoneCountProducerBase7.apply(pitId), game.getPits().get(pitId));
		}
	}

	@Test
	public void assertUpdateRecord() {
		final Integer expectedRecentPit = 3;
		final boolean expectedInProgress = true;
		final Player expectedPlayer = Player.SOUTH;
		final String expectedUrl = "http://dont-care-where.com/games/9871298372"; 

		KalahGameState game = new KalahGameState();
		game.setCurrentPlayer(expectedPlayer);
		game.setInProgress(expectedInProgress);
		game.setRecentPit(expectedRecentPit);
		game.setUrl(expectedUrl);
		for (int idx = 1 ; idx <= 14; idx++) game.getPits().put(idx,stoneCountProducerBase7.apply(idx));

		game = persistenceService.save(game);
		game = persistenceService.findById(game.getGameId()).orElseThrow(() -> new AssertionError("game cannot be retrieved"));

		final Integer updatedRecentPit = (expectedRecentPit + 1) % 14;
		final boolean updatedInProgress = !expectedInProgress;
		final Player updatedPlayer = expectedPlayer.getOpponent();
		final String updatedUrl = "http://somewhere-else/games/9871298372";

		game.setRecentPit(updatedRecentPit);
		game.setInProgress(updatedInProgress);
		game.setCurrentPlayer(updatedPlayer);
		game.setUrl(updatedUrl);

		for (int idx = 1 ; idx <= 14; idx++) game.getPits().put(idx,stoneCountProducerBase11.apply(idx));

		game = persistenceService.save(game);
		Assert.assertEquals(updatedRecentPit, game.getRecentPit());
		Assert.assertEquals(updatedPlayer, game.getCurrentPlayer());
		Assert.assertEquals(updatedInProgress, game.isInProgress());
		Assert.assertEquals(updatedUrl, game.getUrl());

		for (int pitId = 1; pitId <= 14 ; pitId++) {
			Assert.assertEquals(stoneCountProducerBase11.apply(pitId), game.getPits().get(pitId));
		}
		
	}


}
