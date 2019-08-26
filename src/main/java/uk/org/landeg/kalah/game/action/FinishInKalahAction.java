package uk.org.landeg.kalah.game.action;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import uk.org.landeg.kalah.components.KalahGameState;
import uk.org.landeg.kalah.game.KalahGameBoard;

@Order(100)
@Component
public class FinishInKalahAction implements KalahAction{
	Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	KalahGameBoard board;

	@Override
	public boolean applies(KalahGameState game) {
		return (game.getRecentPit() == board.getPlayerKalah().get(game.getCurrentPlayer()));
	}

	/**
	 * Process action for player earning an extra move.
	 * 
	 * We don't actually do anything here, just end the command chain.
	 */
	@Override
	public void processAction(KalahGameState game) {
		log.debug("Player {} taking extra move", game.getCurrentPlayer());
	}

}
