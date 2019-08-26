package uk.org.landeg.kalah.game.action;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import uk.org.landeg.kalah.components.KalahGameState;

/**
 * Determines if the game has finished and finalises the board.
 *
 * @author Andrew Landeg
 *
 */
@Order(1000)
@Component
public class EndTurnKalahAction implements KalahAction {
	Logger log = LoggerFactory.getLogger(this.getClass());

	@Override
	public boolean applies(KalahGameState game) {
		return true;
	}

	@Override
	public void processAction(KalahGameState game) {
		game.setCurrentPlayer(game.getCurrentPlayer().getOpponent());
	}
}
