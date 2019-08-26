package uk.org.landeg.kalah.game.action;

import uk.org.landeg.kalah.components.KalahGameState;

public interface KalahMoveProcessor {
	void processMove(KalahGameState game, int pitId);
}
