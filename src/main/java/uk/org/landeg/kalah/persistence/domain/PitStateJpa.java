package uk.org.landeg.kalah.persistence.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Data
@NoArgsConstructor
@Entity
@Table(name="pit_state")
public class PitStateJpa implements Serializable{
	@Id
	@Column(name="id")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;

	@JoinColumn(name="game_id")
	@ManyToOne(cascade= {})
	private GameStateJpa game;

	@Column(name="pit_id")
	private Integer pitId;

	@Column(name="stones")
	private Integer stones;

	public PitStateJpa(GameStateJpa game, Integer pitId, Integer stones) {
		super();
		this.game = game;
		this.pitId = pitId;
		this.stones = stones;
	}
}
