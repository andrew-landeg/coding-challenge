package uk.org.landeg.kalah.game.action;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import uk.org.landeg.kalah.Constants.Player;
import uk.org.landeg.kalah.components.KalahGameState;
import uk.org.landeg.kalah.game.KalahGameBoard;
import uk.org.landeg.kalah.game.KalahPitDecorator;

/**
 * Determines if the latest move has resulted in a "capture".
 *
 * Event occurs when a players last distributed stone of a move lands
 * in an empty pit on their side of the board.
 *
 * @author Andrew Landeg
 *
 */
@Component
@Order(200)
public class CaptureKalahAction implements KalahAction {
	Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired	
	private KalahGameBoard gameBoard;
	
	@Value("#{new Boolean('${rule.capture_empty_pit.enabled}')}")
	private Boolean allowCaptureOnEmptyOponent;

	/**
	 * Determine if the game state entitles the player to capture stones.
	 * 
	 * The final pit into which a stone was placed must be;
	 *   Previously empty (now has 1 stone)
	 *   Must be on the players side of the board
	 *   Must be opposite a pit which has stones (see note).
	 * 
	 * NOTE this is subject to some ambiguity: should the player capture stone if the opposite pit is empty?
	 */
	@Override
	public boolean applies(KalahGameState game) {
		final KalahPitDecorator pits = game.getPits();
		final int currentPit = game.getRecentPit();
		if (pits.get(currentPit).intValue() != 1) {
			return false;
		}
		final Player currentPlayer = game.getCurrentPlayer();
		if (!gameBoard.getPlayerPits().get(currentPlayer).contains(currentPit)) {
			return false;
		}

		if (allowCaptureOnEmptyOponent) {
			return true;
		}

		final int oppositePitId = gameBoard.getOpposingPits().get(currentPit);
		return (pits.get(oppositePitId) > 0); 
	}

	/**
	 * Process the capture stone move outcome scenario.
	 * 
	 * Player finishes move on an empty pit that they control.
	 * Capture the seed placed in the pit in addition to any seeds in the opposing pit.
	 */
	@Override
	public void processAction(KalahGameState game) {
		final KalahPitDecorator pits = game.getPits();
		final int currentPit = game.getRecentPit();
		final Player currentPlayer = game.getCurrentPlayer();
		final int playerKalah = gameBoard.getPlayerKalah().get(currentPlayer);

		Integer pitToCaptureFrom = gameBoard.getOpposingPits().get(currentPit);
		int stonesPillaged = pits.take(pitToCaptureFrom) + pits.take(currentPit);
		pits.increment(playerKalah, stonesPillaged);
		game.setCurrentPlayer(currentPlayer.getOpponent());
		log.debug("Player {} captured {} stones from pits {} and {}", currentPlayer, stonesPillaged, currentPit, pitToCaptureFrom);
	}

}
