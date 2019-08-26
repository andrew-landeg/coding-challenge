package uk.org.landeg.kalah.game;

import java.util.List;
import java.util.Map;

import uk.org.landeg.kalah.Constants.Player;

/**
 * Defines lookups describing physical characteristics of the board.
 * 
 * @author Andrew Landeg
 *
 */

public interface KalahGameBoard {

	/**
	 * Gets kalah pit ids, mapped by player.
	 *
	 * @return Map of Kalah ids against player
	 */
	Map<Player, Integer> getPlayerKalah();

	/**
	 * Gets list of player pits.
	 * 
	 * @return List of player pits mapped against player.
	 */
	Map<Player, List<Integer>> getPlayerPits();

	/**
	 * Get opposing pit definitions.
	 * 
	 * Quick way of finding which pit to capture from.
	 * 
	 * @return Map of pits against their opposing pits.
	 */
	Map<Integer, Integer> getOpposingPits();

	/**
	 * Sequence of pits a player should follow when distributing stones.
	 * 
	 * @return {@link Map}, list of pit ID's mapped against player. 
	 */
	Map<Player, List<Integer>> getMoveSequence();

	/**
	 * Is the specified pit a Kalah?
	 * 
	 * @param pitId the pitId.
	 * @return <code>true</code> if it's a kalah, <code>false</code> otherwise.
	 */
	boolean isKalah(int pitId);

	/**
	 * Get all pits for either player
	 *
	 * @return List of all playable pits.
	 */
	List<Integer> getAllPlayerPits();

}
