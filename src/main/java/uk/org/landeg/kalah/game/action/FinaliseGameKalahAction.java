package uk.org.landeg.kalah.game.action;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import uk.org.landeg.kalah.Constants.Player;
import uk.org.landeg.kalah.components.KalahGameState;
import uk.org.landeg.kalah.game.KalahGameBoard;
import uk.org.landeg.kalah.game.KalahPitDecorator;

@Order(10000)
@Component
@Slf4j
@RequiredArgsConstructor
public class FinaliseGameKalahAction implements KalahAction {

	private final KalahGameBoard gameBoard;

	@Override
	public boolean applies(KalahGameState game) {
		return checkSideComplete(game, Player.NORTH) || checkSideComplete(game, Player.SOUTH);
	}


	@Override
	public void processAction(KalahGameState game) {
		moveStonesToKalah(game, Player.NORTH);
		moveStonesToKalah(game, Player.SOUTH);
		game.setInProgress(false);
		game.setWinner(determineWinner(game));
	}

	@Override
	public boolean isMutuallyExclusive() {
		return false;
	}

	private boolean checkSideComplete(KalahGameState game, Player player) {
		for (int pitId : gameBoard.getPlayerPits().get(player)) {
			if (game.getPits().get(pitId) > 0) {
				return false;
			}
		}
		return true;
	}

	private Player determineWinner(KalahGameState game) {
		int playerSouthScore = (playerScore(game, Player.SOUTH));
		int playerNorthScore = (playerScore(game, Player.NORTH));
		if (playerNorthScore == playerSouthScore) {
			return Player.NONE;
		}
		return playerSouthScore > playerNorthScore ? Player.SOUTH : Player.NORTH;
	}

	/**
	 * @param game
	 * @param player
	 */
	private int playerScore(KalahGameState game, Player player) {
		return game.getPits().get(gameBoard.getPlayerKalah().get(player));
	}

	private void moveStonesToKalah(KalahGameState game, Player player) {
		final int kalahId = gameBoard.getPlayerKalah().get(player);
		final KalahPitDecorator pits = game.getPits();
		gameBoard.getPlayerPits().get(player).forEach(pitId -> pits.increment(kalahId, pits.take(pitId)));
	}
}
