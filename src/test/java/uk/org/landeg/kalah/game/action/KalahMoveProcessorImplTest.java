package uk.org.landeg.kalah.game.action;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;

import org.mockito.junit.jupiter.MockitoExtension;
import uk.org.landeg.kalah.components.KalahGameState;
import uk.org.landeg.kalah.game.KalahGameBoardStandard;
import uk.org.landeg.kalah.game.KalahGameEngine;
import uk.org.landeg.kalah.game.KalahPitDecorator;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(MockitoExtension.class)
class KalahMoveProcessorImplTest {
	@Spy
	final KalahGameBoardStandard gameBoard = new KalahGameBoardStandard();

	@Spy
	@InjectMocks
	KalahGameEngine gameEngine;

	@InjectMocks
	KalahMoveProcessorImpl action;

	/**
	 * play stone 2 - expected state:
	 * 0 3 3 3 3 3 3 0
	 *   3 0 4 4 4 3
	 */
	@Test
	void assertUnremarkableMoveScenario() {
		final KalahGameState game = new KalahGameState();
		gameEngine.initialiseGame(game, 3);
		action.processMove(game, 2);
		System.out.println(game.getPits());
		assertPitStoneCount(game, Arrays.asList(3, 0, 4, 4, 4, 3, 0, 3, 3, 3, 3, 3, 3, 0));
	}

	/**
	 * play pit 5 - expected state:
	 * 0 3 3 3 3 3 4 1
	 *   3 3 3 3 0 4
	 */
	@Test
	void assertPassOwnKalahScenario() {
		final KalahGameState game = new KalahGameState();
		gameEngine.initialiseGame(game, 3);
		action.processMove(game, 5);
		System.out.println(game.getPits());
		assertPitStoneCount(game, Arrays.asList(3, 3, 3, 3, 0, 4, 1, 4, 3, 3, 3, 3, 3, 0));
	}

	/**
	 * play pit 12 - expected state:
	 * 0 3 3 3 3 3 4 1
	 *   3 3 3 3 0 4
	 */
	@Test
	void assertPassOpponentKalahScenario() {
		final KalahGameState game = new KalahGameState();
		gameEngine.initialiseGame(game, 3);
		action.processMove(game, 5);
		System.out.println(game.getPits());
		assertPitStoneCount(game, Arrays.asList(3, 3, 3, 3, 0, 4, 1, 4, 3, 3, 3, 3, 3, 0));
	}

	void assertPitStoneCount(
			KalahGameState gameState, 
			final List<Integer> expectedStones) {
		final KalahPitDecorator pits = gameState.getPits();
		final List<Integer> offsetStonesToCheck = new ArrayList<>();
		offsetStonesToCheck.add(0);
		offsetStonesToCheck.addAll(expectedStones);
		for (int idx = 1 ; idx <= pits.size(); idx++) {
			int actual = gameState.getPits().get(idx);
			int expected = offsetStonesToCheck.get(idx);
			assertThat(expected).isEqualTo(actual);
		}
	}
}
