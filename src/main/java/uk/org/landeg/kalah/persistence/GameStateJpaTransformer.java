package uk.org.landeg.kalah.persistence;

import java.util.HashMap;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import uk.org.landeg.kalah.annotation.JpaTransformerComponent;
import uk.org.landeg.kalah.components.KalahGameState;
import uk.org.landeg.kalah.game.KalahPitDecorator;
import uk.org.landeg.kalah.persistence.domain.GameStateJpa;
import uk.org.landeg.kalah.persistence.domain.PitStateJpa;

@Component
@JpaTransformerComponent(GameStateJpa.class)
public class GameStateJpaTransformer implements JpaTransformer<GameStateJpa, KalahGameState> {
    /**
     * {@inheritDoc}
     */
    @Override
    public KalahGameState fromJpa(GameStateJpa source, KalahGameState target) {
    	final KalahGameState response = (target == null) ? new KalahGameState() : target;
    	BeanUtils.copyProperties(source, response, "pits"); 
        source.getPits().values().forEach(pitJpa -> 
        	response.getPits().put(pitJpa.getPitId(), pitJpa.getStones()));
        return response;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public GameStateJpa toJpa(KalahGameState source, GameStateJpa target) {
    	final GameStateJpa response = (target == null) ? new GameStateJpa() : target;

    	BeanUtils.copyProperties(source, response, "pits");
		final KalahPitDecorator gameStatePits = source.getPits();

		gameStatePits.entrySet().forEach(e -> {
			Integer pitId = e.getKey();
			Integer stoneCount = e.getValue();
			if (response.getPits() == null) {
				response.setPits(new HashMap<Integer, PitStateJpa>());
			}
			PitStateJpa pitJpa = response.getPits().get(pitId);
			// update the pit record stone count, create new record if needed.
			if (pitJpa == null) {
				pitJpa = new PitStateJpa(target, pitId, stoneCount);
			} else {
				pitJpa.setStones(stoneCount);
			}
			response.getPits().put(pitId, pitJpa);
		});
		return response;
    }
}
