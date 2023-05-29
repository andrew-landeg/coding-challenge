package uk.org.landeg.kalah.game.action;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import uk.org.landeg.kalah.Constants.Player;
import uk.org.landeg.kalah.components.KalahGameState;
import uk.org.landeg.kalah.game.KalahGameBoard;
import uk.org.landeg.kalah.game.KalahPitDecorator;

@Component
@RequiredArgsConstructor
public class KalahMoveProcessorImpl implements KalahMoveProcessor{
	private final KalahGameBoard gameBoard;

	@Override
	public void processMove(KalahGameState game, int pitId) {
		KalahPitDecorator pits = game.getPits();

		int stonesToDistribute = pits.take(pitId);
		int currentPit = pitId;
		while(stonesToDistribute > 0) {
			currentPit = getNextPit(currentPit, game.getCurrentPlayer());
			pits.increment(currentPit, 1);
			stonesToDistribute--;
		}
		game.setRecentPit(currentPit);		
	}

	private Integer getNextPit(Integer pit, Player player) {
		List<Integer> sequence = gameBoard.getMoveSequence().get(player);
		final int index = (sequence.indexOf(pit) + 1) % sequence.size();
		return sequence.get(index);
	}
}
