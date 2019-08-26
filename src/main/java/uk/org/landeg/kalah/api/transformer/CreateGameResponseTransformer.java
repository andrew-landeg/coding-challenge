package uk.org.landeg.kalah.api.transformer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.org.landeg.kalah.annotation.RestTransformer;
import uk.org.landeg.kalah.api.model.CreateGameResponseModel;
import uk.org.landeg.kalah.components.KalahGameState;
import uk.org.landeg.kalah.exception.KalahException;

/**
 * Produces {@link CreateGameResponseModel} from {@link KalahGameState}
 *
 * @author Andrew Landeg
 *
 */
@RestTransformer(restEntity=CreateGameResponseModel.class)
public class CreateGameResponseTransformer implements RestEntityTransformer<CreateGameResponseModel, KalahGameState>{
	Logger log = LoggerFactory.getLogger(this.getClass());

	/**
	 * {@inheritDoc}
	 */
	@Override
	public CreateGameResponseModel toRest(KalahGameState game) {
		log.debug("CreateGameResponseModel {}", game);
		if (game == null) {
			throw new KalahException("attempting to serialize null game");
		}
		return new CreateGameResponseModel()
				.withId(game.getGameId())
				.withUri(game.getUrl());
	}
}
