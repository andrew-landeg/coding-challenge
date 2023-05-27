package uk.org.landeg.kalah.game.action;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
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
import uk.org.landeg.kalah.game.KalahGameEngine;
import uk.org.landeg.kalah.game.KalahPitDecorator;

@ExtendWith(SpringExtension.class)
@Import(CommonTestConfiguration.class)
class FinaliseGameKalahActionTest {
	@TestConfiguration
	static class Config {
		@Bean
		@Autowired
		KalahAction finaliseGameKalahAction(KalahGameBoard gameBoard) {
			return new FinaliseGameKalahAction(gameBoard);
		}
	}

	@Autowired
	private KalahGameBoard gameBoard;

	@Autowired
	private KalahGameEngine gameService;

	@Autowired
	private KalahAction finaliseAction;

	@Mock
	KalahGameState game;

	@Mock
	KalahPitDecorator pits;

	@BeforeEach
	void setup() {
		when(game.getPits()).thenReturn(pits);
	}

	@Test
	void assertApplicableForZeroNorthStones() {
		when(pits.get(Mockito.any())).thenAnswer(invocation -> {
			Integer pitId = invocation.getArgument(0);
			return gameBoard.getPlayerPits().get(Player.NORTH).contains(pitId) ? 0 : 1;
		});
		assertThat(finaliseAction.applies(game)).isTrue();
	}

	@Test
	void assertApplicableForZeroSouthStones() {
		when(pits.get(Mockito.any())).thenAnswer(invocation -> {
			Integer pitId = invocation.getArgument(0);
			return gameBoard.getPlayerPits().get(Player.SOUTH).contains(pitId) ? 0 : 1;
		});
		assertThat(finaliseAction.applies(game)).isTrue();
	}

	@Test
	void assertNotApplicableWhenStonesRemain() {
		when(pits.get(Mockito.any())).thenAnswer(invocation -> {
			Integer pitId = invocation.getArgument(0);
			int southPit = gameBoard.getPlayerPits().get(Player.SOUTH).get(0);
			int northPit = gameBoard.getPlayerPits().get(Player.NORTH).get(0);
			// simulate one stone of each side of the board. 
			return (pitId == southPit || pitId == northPit) ? 1 : 0;
		});
		assertThat(finaliseAction.applies(game)).isFalse();
	}


	@Test
	void processingSuccessful() {
		final KalahGameState game = new KalahGameState();
		gameService.initialiseGame(game);
		// put 1 stone in each south pit, 2 in each north pit
		final int expectedSouthStones = 6 * 1;
		final int expectedNorthStones = 6 * 2;

		final KalahPitDecorator pits = game.getPits();
		gameBoard.getPlayerPits().get(Player.SOUTH).forEach(pitId -> pits.put(pitId, 1));
		gameBoard.getPlayerPits().get(Player.NORTH).forEach(pitId -> pits.put(pitId, 2));
		
		finaliseAction.processAction(game);

		var northKalah = gameBoard.getPlayerKalah().get((Player.NORTH));
		var southKalah = gameBoard.getPlayerKalah().get((Player.SOUTH));
		assertThat(pits)
				.containsEntry(northKalah, expectedNorthStones)
				.containsEntry(southKalah, expectedSouthStones);
	}
}
