package uk.org.landeg.kalah.game.action;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import uk.org.landeg.kalah.components.KalahGameState;
import uk.org.landeg.kalah.game.KalahGameBoard;

@Order(100)
@Component
@Slf4j
@RequiredArgsConstructor
public class FinishInKalahAction implements KalahAction{
	private final KalahGameBoard board;

	@Override
	public boolean applies(KalahGameState game) {
		var pit = board.getPlayerKalah().get(game.getCurrentPlayer());
		return (game.getRecentPit().equals(pit));
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
