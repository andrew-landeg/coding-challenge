package uk.org.landeg.kalah.persistence;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import uk.org.landeg.kalah.components.KalahGameState;

//@Service
public class PersistenceServiceMemory implements PersistenceService {
	Map<Long,KalahGameState> games = new HashMap<>();
	
	@Override
	public KalahGameState save(KalahGameState game) {
		if (game.getGameId() == null) {
			Long id = System.nanoTime();
			game.setGameId(id);
		}
		games.put(game.getGameId(), game);
		return game;
	}

	@Override
	public Optional<KalahGameState> findById(Long id) {
		return Optional.ofNullable(games.get(id));
	}
}
