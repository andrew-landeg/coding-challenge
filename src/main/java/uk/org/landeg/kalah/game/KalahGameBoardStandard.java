package uk.org.landeg.kalah.game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import uk.org.landeg.kalah.Constants.Player;

/**
 * Defines lookups describing physical characteristics of the board.
 * 
 * This defines a standard Kalah game.
 * @author Andrew Landeg
 *
 */
@Component
public class KalahGameBoardStandard implements KalahGameBoard {
	/** default initial number stones (game variant) */ 
	public static final Integer DEFAULT_INIT_STONES = 6;
	/** first pit ID. */
	public static final Integer MIN_PIT_ID = 1;
	/** last pit ID. */
	public static final Integer MAX_PIT_ID = 14;
	/** Kalah/House ID. */
	public static final Integer SOUTH_KALAH = 7;
	/** Kalah/House ID. */
	public static final Integer NORTH_KALAH = 14;
	/** Playable pits per player. */
	public static final int PLAYER_PIT_COUNT = 6;
	
	public static KalahGameBoard INSTANCE = null;

	/** define which player owns which pit.*/
	private Map<Player, List<Integer>> playerPits = new HashMap<>();
	/** define which player owns which kalah. */
	private Map<Player, Integer> playerKalah = new HashMap<>();
	/** define the opposing pits. */
	private Map<Integer, Integer> opposingPits = new HashMap<>();
	/** define move sequence when distributing stones. */
	private Map<Player, List<Integer>> moveSequence = new HashMap<>();

	public KalahGameBoardStandard() {
		setupPlayerPits();
		setupKalahPists();
		setupOpposingPits();
		setupMoveSequence();
		if (INSTANCE == null) {
			setInstance(this);
		}
	}

	private static synchronized void setInstance(KalahGameBoardStandard instance) {
		if (INSTANCE == null) {
			INSTANCE = instance;
		}
	}

	@Override
	public Map<Player, Integer> getPlayerKalah() {
		return playerKalah;
	}
	
	@Override
	public Map<Player, List<Integer>> getPlayerPits() {
		return playerPits;
	}
	
	@Override
	public Map<Integer, Integer> getOpposingPits() {
		return opposingPits;
	}

	@Override
	public Map<Player, List<Integer>> getMoveSequence() {
		return moveSequence;
	}

	@Override
	public boolean isKalah(int pitId) {
		return pitId == NORTH_KALAH || pitId == SOUTH_KALAH;
	}

	@Override
	public List<Integer> getAllPlayerPits() {
		final List<Integer> allPits = new ArrayList<>();
		allPits.addAll(playerPits.get(Player.SOUTH));
		allPits.addAll(playerPits.get(Player.NORTH));
		return allPits;
	}

	/**
	 * defines which player owns which pit
	 */
	private void setupPlayerPits() {
		final List<Integer> southPlayerPits = Arrays.asList(1,2,3,4,5,6);
		final List<Integer> northPlayerPits = Arrays.asList(8,9,10,11,12,13);
		playerPits.put(Player.SOUTH, southPlayerPits);
		playerPits.put(Player.NORTH, northPlayerPits);
	}

	/**
	 * defines movement sequence for distributing stones. 
	 */
	private void setupMoveSequence() {
		final List<Integer> moveSequeneSouth = new ArrayList<>();
		final List<Integer> moveSequeneNorth = new ArrayList<>();
		for (int pitId = 1 ; pitId <= MAX_PIT_ID ; pitId++) {
			if (NORTH_KALAH!= pitId) {
				moveSequeneSouth.add(pitId);
			}
			if (SOUTH_KALAH != pitId) {
				moveSequeneNorth.add(pitId);
			}
		}
		moveSequence.put(Player.SOUTH, moveSequeneSouth);
		moveSequence.put(Player.NORTH, moveSequeneNorth);
	}


	/**
	 * setup pit relationships
	 */
	private void setupOpposingPits() {
		for (int offset = 0 ; offset < PLAYER_PIT_COUNT ; offset++) {
			int south = 1 + offset;
			int north = 13 - offset;
			opposingPits.put(north, south);
			opposingPits.put(south, north);
		}
	}


	/**
	 * setup kalah pits.
	 */
	private void setupKalahPists() {
		playerKalah.put(Player.SOUTH, SOUTH_KALAH);
		playerKalah.put(Player.NORTH, NORTH_KALAH);
	}
}
