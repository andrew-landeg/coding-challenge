package uk.org.landeg.kalah.components;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import uk.org.landeg.kalah.Constants.Player;
import uk.org.landeg.kalah.game.KalahPitDecorator;

/**
 * Represents the state of a single Kalah game...
 * 
 * @author Andrew Landeg
 *
 */
@Slf4j
@NoArgsConstructor
@Data
@Component
@Scope(proxyMode=ScopedProxyMode.TARGET_CLASS, scopeName="SCOPE_PROTOTYPE")
public class KalahGameState {
	/**
	 * game ID
	 */
	private Long gameId;
	/**
	 * the pits - value represents the number of stones in each pit.
	 */
	private KalahPitDecorator pits = new KalahPitDecorator();
	/**
	 * Player expected to move next
	 */
	private Player currentPlayer = Player.SOUTH;
	/**
	 * <code>true</code> indicates the game is in progress, <code>false</code> that it has finished. 
	 */
	private boolean inProgress;
	
	/**
	 * set when the player wins
	 */
	private Player winner;

	/**
	 * most recent pit to have a stone placed in.
	 */
	private Integer recentPit = null;

	/**
	 * url for this game. 
	 */
	private String url;
}
