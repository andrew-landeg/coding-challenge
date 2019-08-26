package uk.org.landeg.kalah.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import uk.org.landeg.kalah.annotation.RestTransformer;
import uk.org.landeg.kalah.api.model.CreateGameResponseModel;
import uk.org.landeg.kalah.api.model.PerformMoveResponseModel;
import uk.org.landeg.kalah.api.transformer.RestEntityTransformer;
import uk.org.landeg.kalah.components.KalahGameState;
import uk.org.landeg.kalah.exception.KalahGameNotFoundException;
import uk.org.landeg.kalah.game.KalahGameBoard;
import uk.org.landeg.kalah.game.KalahGameEngine;
import uk.org.landeg.kalah.persistence.PersistenceService;
import uk.org.landeg.kalah.resolver.RequestUriResolver;

@Service
public class KalahService {
	@Autowired
	KalahGameEngine gameService;

	@Autowired
	KalahGameBoard gameBoard;

	@Autowired
	PersistenceService persistenceService;

	@Autowired
	RequestUriResolver uriResolver;

	@Autowired
	@RestTransformer(restEntity = PerformMoveResponseModel.class)
	RestEntityTransformer<PerformMoveResponseModel, KalahGameState> performMoveTransformer;

	@Autowired
	@RestTransformer(restEntity=CreateGameResponseModel.class)
	RestEntityTransformer<CreateGameResponseModel, KalahGameState> createGameTransformer;

	/**
	 * Create a new Kalah game instance.
	 *
	 * @param initialStones the stone count variant of this game.
	 * @return {@link CreateGameResponseModel}
	 */
	public CreateGameResponseModel createGame(Integer initialStones) {
		KalahGameState game = new KalahGameState();
		gameService.initialiseGame(game, initialStones);
		
		game = persistenceService.save(game);
		final String gameEndpoint = new StringBuilder().append(uriResolver.resolve())
				.append("/")
				.append(Long.toString(game.getGameId()))
				.toString();
		game.setUrl(gameEndpoint);
		game = persistenceService.save(game);
		return createGameTransformer.toRest(game);
	}

	public KalahGameState retrieveGame(Long gameId) {
		return retrieveGameInternal(gameId);
	}

	public PerformMoveResponseModel makeMove(Long gameId, Integer pitId) {
		KalahGameState game = retrieveGameInternal(gameId);
		gameService.performMove(game, pitId);
		game = persistenceService.save(game);
		return performMoveTransformer.toRest(game);
	}

	private KalahGameState retrieveGameInternal(final Long gameId) {
		return persistenceService.findById(gameId).orElseThrow(() -> new KalahGameNotFoundException());
	}
}
