package uk.org.landeg.kalah.game.action;

import uk.org.landeg.kalah.components.KalahGameState;

public interface KalahAction {
	/**
	 * Determines if this action should be performed on the specified game state.
	 *
	 * @param game the game state
	 * @return true if this action should be applied
	 */
	boolean applies(KalahGameState game);

	/**
	 * Perform the action on the specified game state.
	 * 
	 * @param game game to which action should be applied.
	 */
	void processAction(KalahGameState game);

	/**
	 * Indicates that this action is mutually exclusive.
	 * 
	 * Most Kalah move outcomes are mutually exclusive (Either capture OR extra turn OR end move).
	 * Some actions are not mutually exclusive (specifically end game) in which case
	 * we need to check for end game, regardless of other actions being completed.
	 * 
	 * @return <code>true</code> if the action is mutually exclusive <code>false</code> otherwise.
	 */
	default boolean isMutuallyExclusive () {
		return true;
	}
}
