package uk.org.landeg.kalah.persistence;

import java.util.Optional;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import uk.org.landeg.kalah.annotation.JpaTransformerComponent;
import uk.org.landeg.kalah.components.KalahGameState;
import uk.org.landeg.kalah.persistence.domain.GameStateJpa;

/**
 * Provides JPA persistence.
 * 
 * @author andy
 *
 */
@Service
@RequiredArgsConstructor
public class PersistenceServiceJpa implements PersistenceService {
    private final GameStateRepository repo;

	@JpaTransformerComponent(GameStateJpa.class)
	private final JpaTransformer<GameStateJpa, KalahGameState> gameStateTransformer;
	
	/**
	 * Find the game state with the specified ID
	 * 
	 * @param id the id of the game to find
	 * @return {@link Optional} of a {@link KalahGameState}
	 */
	@Override
	@Transactional
	public Optional<KalahGameState> findById(Long id) {
		return repo.findById(id)
			.map(jpa -> gameStateTransformer.fromJpa(jpa, new KalahGameState()))
			.map(Optional::of)
			.orElse(Optional.empty());
	}

	/**
	 * Saves the specified game state with associated pits
	 * 
	 * @param game the game to save.
	 * @return the a {@link KalahGameState} representing the saved instance of the game.
	 * 
	 */
	@Override
	@Transactional
	public KalahGameState save(KalahGameState game) {
		GameStateJpa jpa;
		if (game.getGameId() == null) {
			jpa = new GameStateJpa();
		} else {
			jpa = repo.findById(game.getGameId())
					.orElse(new GameStateJpa());
		}
		gameStateTransformer.toJpa(game, jpa);
		jpa = repo.save(jpa);
		return gameStateTransformer.fromJpa(jpa, null);
	}
}
