package uk.org.landeg.kalah.persistence;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface GameStateRepository extends CrudRepository<GameStateJpa, Long>{
	@Query("SELECT g FROM GameStateJpa g LEFT JOIN FETCH g.pits WHERE g.gameId = ?1")
	Optional<GameStateJpa> findById(Long id);
}
