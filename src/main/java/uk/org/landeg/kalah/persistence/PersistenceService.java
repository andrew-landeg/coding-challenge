package uk.org.landeg.kalah.persistence;

import java.util.Optional;

import org.springframework.stereotype.Service;

import uk.org.landeg.kalah.components.KalahGameState;

/**
 * Simple storage service for game state.
 * 
 * @author Andrew Landeg
 *
 */
@Service
public interface PersistenceService {

	Optional<KalahGameState> findById(Long id);

	KalahGameState save(KalahGameState game);

}
