package uk.org.landeg.kalah.persistence.domain;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MapKey;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Data;
import lombok.NoArgsConstructor;
import uk.org.landeg.kalah.Constants.Player;

@Data
@NoArgsConstructor
@Entity
@Table(name="game_state")
public class GameStateJpa {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="game_id")
	private Long gameId;

	@Enumerated(EnumType.STRING)
	@Column(name="current_player")
	private Player currentPlayer = Player.SOUTH;

	@Column(name="in_progress")
	private Boolean inProgress;
	
	@Enumerated(EnumType.STRING)
	@Column(name="winner")
	private Player winner;

	@Column(name="recentPit")
	private Integer recentPit = null;
	
	@Column(name="url")
	private String url;

	@OneToMany(cascade=CascadeType.ALL, mappedBy="game")
	@MapKey(name="pitId")
	Map<Integer, PitStateJpa> pits = new HashMap<>();
}
