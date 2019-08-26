package uk.org.landeg.kalah.persistence.domain;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MapKey;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import uk.org.landeg.kalah.Constants.Player;

@Entity
@Table(name="game_state")
public class GameStateJpa {
	@Id
	@GeneratedValue
	@Column(name="game_id")
	private Long gameId;

	@Enumerated(EnumType.STRING)
	@Column(name="current_player")
	private Player currentPlayer = Player.SOUTH;

	@Column(name="in_progress")
	private Boolean inProgress;
	
	@Column(name="winner")
	private Player winner;

	@Column(name="recentPit")
	private Integer recentPit = null;
	
	@Column(name="url")
	private String url;


//	@OneToMany(cascade=CascadeType.ALL, mappedBy="gameId")
//	@JoinColumn(name="game_id")
//	@MapKey(name="pitId")
//	Map<Integer, PitStateJpa> pits = new HashMap<>();

	@OneToMany(cascade=CascadeType.ALL, mappedBy="game")
	@MapKey(name="pitId")
	Map<Integer, PitStateJpa> pits = new HashMap<>();
	
	public Long getGameId() {
		return gameId;
	}

	public void setGameId(Long gameId) {
		this.gameId = gameId;
	}

	public Player getCurrentPlayer() {
		return currentPlayer;
	}

	public void setCurrentPlayer(Player currentPlayer) {
		this.currentPlayer = currentPlayer;
	}

	public Boolean getInProgress() {
		return inProgress;
	}

	public void setInProgress(Boolean inProgress) {
		this.inProgress = inProgress;
	}

	public Player getWinner() {
		return winner;
	}

	public void setWinner(Player winner) {
		this.winner = winner;
	}

	public Integer getRecentPit() {
		return recentPit;
	}

	public void setRecentPit(Integer recentPit) {
		this.recentPit = recentPit;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Map<Integer, PitStateJpa> getPits() {
		return pits;
	}

	public void setPits(Map<Integer, PitStateJpa> pits) {
		this.pits = pits;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("GameStateJpa [gameId=");
		builder.append(gameId);
		builder.append(", currentPlayer=");
		builder.append(currentPlayer);
		builder.append(", inProgress=");
		builder.append(inProgress);
		builder.append(", winner=");
		builder.append(winner);
		builder.append(", recentPit=");
		builder.append(recentPit);
		builder.append(", url=");
		builder.append(url);
		builder.append(", pits=");
		builder.append(pits);
		builder.append("]");
		return builder.toString();
	}
}
