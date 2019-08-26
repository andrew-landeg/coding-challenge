package uk.org.landeg.kalah.components;

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

	public KalahPitDecorator getPits() {
		return pits;
	}
	public void setPits(KalahPitDecorator pits) {
		this.pits = pits;
	}
	public Player getCurrentPlayer() {
		return currentPlayer;
	}
	public void setCurrentPlayer(Player currentPlayer) {
		this.currentPlayer = currentPlayer;
	}
	public Long getGameId() {
		return gameId;
	}
	public void setGameId(Long gameId) {
		this.gameId = gameId;
	}
	public boolean isInProgress() {
		return inProgress;
	}
	public void setInProgress(boolean inProgress) {
		this.inProgress = inProgress;
	}
	public void setWinner(Player winner) {
		this.winner = winner;
	}
	public Player getWinner() {
		return winner;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public Integer getRecentPit() {
		return recentPit;
	}
	public void setRecentPit(Integer recentPit) {
		this.recentPit = recentPit;
	}
}
