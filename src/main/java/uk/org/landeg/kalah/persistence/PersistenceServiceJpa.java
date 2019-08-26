package uk.org.landeg.kalah.persistence;

import java.util.Optional;



import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import uk.org.landeg.kalah.components.KalahGameState;
import uk.org.landeg.kalah.game.KalahPitDecorator;

@Service
public class PersistenceServiceJpa implements PersistenceService{
	@Autowired
	GameStateRepository repo;
	
	@Override
	@Transactional
	public Optional<KalahGameState> findById(Long id) {
		return repo.findById(id)
			.map(jpa -> fromJpa(jpa, new KalahGameState()))
			.map(Optional::of)
			.orElse(Optional.empty());
	}

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
		toJpa(game, jpa);
		jpa = repo.save(jpa);
		return fromJpa(jpa, new KalahGameState()); 
	}
	
	
	//TODO - [time constraint] should really separate this out into separate transformer class.
	// This will be easier to maintain and unit tests.
	private KalahGameState fromJpa(final GameStateJpa jpa, final KalahGameState business) {
		BeanUtils.copyProperties(jpa, business);
		jpa.getPits().values().forEach(pitJpa -> business.getPits().put(pitJpa.getPitId(), pitJpa.getStones()));
		return business;
	}

	//TODO - [time constraint] should really separate this out into separate transformer class.
	// This will be easier to maintain and unit tests.
	private GameStateJpa toJpa(final KalahGameState business, final GameStateJpa jpa) {
		jpa.setCurrentPlayer(business.getCurrentPlayer());
		jpa.setInProgress(business.isInProgress());
		jpa.setRecentPit(business.getRecentPit());
		jpa.setWinner(business.getWinner());
		jpa.setUrl(business.getUrl());
		jpa.setGameId(business.getGameId());
		final KalahPitDecorator pits = business.getPits();
		pits.entrySet().forEach(e -> {
			Integer pitId = e.getKey();
			Integer stoneCount = e.getValue();
			PitStateJpa pitJpa = jpa.getPits().get(pitId);
			// update the pit record stone count, create new record if needed.
			if (pitJpa == null) {
				pitJpa = new PitStateJpa(jpa, pitId, stoneCount);
			} else {
				pitJpa.setStones(stoneCount);
			}
			jpa.getPits().put(pitId, pitJpa);
		});
		return jpa;
	}
}
