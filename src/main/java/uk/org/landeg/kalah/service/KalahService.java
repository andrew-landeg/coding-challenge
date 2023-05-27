package uk.org.landeg.kalah.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import uk.org.landeg.kalah.annotation.RestTransformer;
import uk.org.landeg.kalah.api.model.CreateGameResponseModel;
import uk.org.landeg.kalah.api.model.PerformMoveResponseModel;
import uk.org.landeg.kalah.api.transformer.RestEntityTransformer;
import uk.org.landeg.kalah.components.KalahGameState;
import uk.org.landeg.kalah.exception.KalahClientException;
import uk.org.landeg.kalah.exception.KalahGameNotFoundException;
import uk.org.landeg.kalah.game.KalahGameBoard;
import uk.org.landeg.kalah.game.KalahGameEngine;
import uk.org.landeg.kalah.persistence.PersistenceService;
import uk.org.landeg.kalah.resolver.RequestUriResolver;

@Service
@RequiredArgsConstructor
public class KalahService {
	private final KalahGameEngine gameService;

	private final KalahGameBoard gameBoard;

	private final PersistenceService persistenceService;

	private final RequestUriResolver uriResolver;

	@RestTransformer(restEntity = PerformMoveResponseModel.class)
	private final RestEntityTransformer<PerformMoveResponseModel, KalahGameState> performMoveTransformer;

	@RestTransformer(restEntity=CreateGameResponseModel.class)
	private final RestEntityTransformer<CreateGameResponseModel, KalahGameState> createGameTransformer;

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

	/**
	 * Retrieve the game the the specified ID
	 * 
	 * @param gameId id of the game to retrieve
	 * @return {@link KalahGameState} representation of the stored game
	 * @throws KalahGameNotFoundException when game is not found.
	 */
	public KalahGameState retrieveGame(Long gameId) {
		return retrieveGameInternal(gameId);
	}

	/**
	 * Performs a move on the game and pit with the specified ID's.
	 *
	 * @param gameId id of the game on which to perform a move
	 * @param pitId id of the pit on which to perform a move
	 * 
	 * @return PerformMoveResponseModel describing the updated state of the board.
	 * 
	 * @throws KalahGameNotFoundException when game is not found.
	 * @throws KalahClientException for illegal moves.
	 */
	public PerformMoveResponseModel makeMove(Long gameId, Integer pitId) {
		KalahGameState game = retrieveGameInternal(gameId);
		gameService.performMove(game, pitId);
		game = persistenceService.save(game);
		return performMoveTransformer.toRest(game);
	}

	private KalahGameState retrieveGameInternal(final Long gameId) {
		return persistenceService.findById(gameId)
				.orElseThrow(KalahGameNotFoundException::new);
	}
}
