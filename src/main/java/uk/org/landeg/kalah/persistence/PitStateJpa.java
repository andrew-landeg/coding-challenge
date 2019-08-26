package uk.org.landeg.kalah.persistence;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="pit_state")
public class PitStateJpa implements Serializable{
	private static final long serialVersionUID = -7778361844563421433L;
	@Id
	@Column(name="id")
	@GeneratedValue
	private Long id;

	@JoinColumn(name="game_id")
	@ManyToOne(cascade= {})
	private GameStateJpa game;

	@Column(name="pit_id")
	private Integer pitId;

	@Column(name="stones")
	private Integer stones;

	public PitStateJpa() {
	}

	
	
	public PitStateJpa(GameStateJpa game, Integer pitId, Integer stones) {
		super();
		this.game = game;
		this.pitId = pitId;
		this.stones = stones;
	}



	public GameStateJpa getGame() {
		return game;
	}


	public void setGame(GameStateJpa game) {
		this.game = game;
	}


	public Integer getPitId() {
		return pitId;
	}
	public void setPitId(Integer pitId) {
		this.pitId = pitId;
	}
	public Integer getStones() {
		return stones;
	}
	public void setStones(Integer stones) {
		this.stones = stones;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}



	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("PitStateJpa [id=");
		builder.append(id);
		builder.append(", game=");
		builder.append(game.getGameId());
		builder.append(", pitId=");
		builder.append(pitId);
		builder.append(", stones=");
		builder.append(stones);
		builder.append("]");
		return builder.toString();
	}
	
	
}
