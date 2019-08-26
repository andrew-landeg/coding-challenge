package uk.org.landeg.kalah.api.transformer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.org.landeg.kalah.annotation.RestTransformer;
import uk.org.landeg.kalah.api.model.PerformMoveResponseModel;
import uk.org.landeg.kalah.components.KalahGameState;
import uk.org.landeg.kalah.exception.KalahException;

@RestTransformer(restEntity = PerformMoveResponseModel.class)
public class PerformMoveResponseTransformer implements RestEntityTransformer<PerformMoveResponseModel, KalahGameState> {
	Logger log = LoggerFactory.getLogger(this.getClass());
	

	/**
	 * Construct a {@link PerformMoveResponseModel} from a {@link KalahGameState}
	 * @param game source object
	 * @return {@link PerformMoveResponseModel} model object 
	 */
	@Override
	public PerformMoveResponseModel toRest(KalahGameState game) {
		log.debug("toRest {}", game);
		if (game == null) {
			throw new KalahException("attempting to serialize null game");
		}
		final PerformMoveResponseModel response = new PerformMoveResponseModel()
				.withId(game.getGameId())
				.withUri(game.getUrl());
		game.getPits().entrySet().forEach(e -> {
			response.withPitStatus(e.getKey(), e.getValue());
		});
		return response;
	}
}
