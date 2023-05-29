package uk.org.landeg.kalah.game;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import uk.org.landeg.kalah.Constants.Player;
import uk.org.landeg.kalah.components.KalahGameState;
import uk.org.landeg.kalah.exception.KalahClientException;
import uk.org.landeg.kalah.game.action.KalahAction;
import uk.org.landeg.kalah.game.action.KalahMoveProcessor;

/**
 * The main game engine.
 * 
 * Processes events; initialising the game and performing moves.
 *
 * @author Andrew Landeg
 *
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class KalahGameEngine {
	private final KalahGameBoard gameBoard;
	private final List<KalahAction> kalahActions;
	private final KalahMoveProcessor moveProcessor;

	private List<KalahAction> exclusiveActions = new ArrayList<>();
	private List<KalahAction> independentActions = new ArrayList<>();

	public void initialiseGame(KalahGameState game) {
		this.initialiseGame(game, KalahGameBoardStandard.DEFAULT_INIT_STONES);
	}

	@PostConstruct
	public void initialiseProcessors() {
		// would have been nice to discriminate by qualifier rather than sorting here...
		kalahActions.forEach(action ->
			(action.isMutuallyExclusive() ? exclusiveActions : independentActions).add(action));
	}

	public void initialiseGame(KalahGameState game, int initialStones) {
		log.debug("initialiseGame {}", initialStones);
		game.getPits().clear();
		game.setInProgress(true);
		for(int i = 1 ; i <= KalahGameBoardStandard.MAX_PIT_ID ; i++ ) {
			game.getPits().put(i, gameBoard.isKalah(i) ? 0 : initialStones);
		}
	}

	/**
	 * Performs the move on the specified pit.
	 * 
	 * If the current player is not known, it shall be inferred from the pit being played.
	 * 
	 * improvement: We may want to return the move outcome to enhance player experience.
	 * 
	 * @param game game on which to operate
	 * @param pitId id of the pit to start the move.
	 */
	public void performMove(KalahGameState game, Integer pitId) {
		Player currentPlayer = game.getCurrentPlayer();
		if (currentPlayer == null) {
			log.debug("player not set, inferring from pit {}", pitId);
			currentPlayer = inferCurrentPlayer(pitId);
			game.setCurrentPlayer(currentPlayer);
		}

		if (!game.isInProgress()) {
			throw new KalahClientException("That game has already ended");
		}
		if (!gameBoard.getPlayerPits().get(currentPlayer).contains(pitId)) {
			throw new KalahClientException("You can only play pits you control (perhaps it's not your turn?)");
		}
		if (game.getPits().get(pitId) == 0) {
			throw new KalahClientException("You can't start a move from an empty pit");
		}

		moveProcessor.processMove(game, pitId);

		// command chain, modified to allow mutually exclusive actions to be 
		// run, regardless of previous actions.
		processActions(game, exclusiveActions, true);
		processActions(game, independentActions, false);
	}

	/**
	 * Apply actions to the specified game sate.
	 *
	 * @param game game to apply actions to 
	 * @param actions actions to process
	 * @param exclusive treat actions as mutually exclusive?
	 */
	private void processActions(
			final KalahGameState game, 
			final List<KalahAction> actions, 
			boolean exclusive) {
		for (KalahAction action : actions) {
			log.debug("checking action {}", action.getClass());
				if (action.applies(game)) {
					log.debug("applying action {}", action.getClass());
					action.processAction(game);
					if (exclusive) {
						break;
					}
				}
			}
		}

	private Player inferCurrentPlayer(Integer pitId) {
		Player player = Player.SOUTH;
		if (gameBoard.getPlayerPits().get(Player.NORTH).contains(pitId)) {
			player = Player.NORTH;
		}
		log.debug("Inferred that it's {}'s turn....", player.name() );
		return player;
	}
}
