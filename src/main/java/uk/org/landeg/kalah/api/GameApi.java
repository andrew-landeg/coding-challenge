package uk.org.landeg.kalah.api;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import uk.org.landeg.kalah.api.model.CreateGameResponseModel;
import uk.org.landeg.kalah.api.model.PerformMoveResponseModel;
import uk.org.landeg.kalah.components.KalahGameState;
import uk.org.landeg.kalah.game.KalahGameBoardStandard;
import uk.org.landeg.kalah.service.KalahService;
import uk.org.landeg.kalah.validator.ValidPitIndex;

/**
 * API providing services for playing Kalah.
 *
 * @author Andrew Landeg
 *
 */
@Validated
@RestController
@RequestMapping("/games")
public class GameApi {
	Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired KalahService kalahWebService;

	/**
	 * Requests a new, initialised game be created.
	 *
	 * @param initialStonesOptional initial number of stones.  This is beyond spec and should not be used.
	 * @return CreateGameResponseModel describing the game location and id.
	 */
	@PostMapping("")
	public ResponseEntity<CreateGameResponseModel> createGame(
			@RequestParam("stones") Optional<Integer> initialStonesOptional) {
		log.debug("createGame {}", initialStonesOptional);
		int initialStones = initialStonesOptional.orElse(KalahGameBoardStandard.DEFAULT_INIT_STONES);
		final CreateGameResponseModel response = kalahWebService.createGame(initialStones); 
		return ResponseEntity.status(HttpStatus.CREATED.value())
				.body(response);
	}

	/**
	 * Retrieves game state.
	 * 
	 * @param id id of the game to retrieve
	 * @return the game state.
	 * @deprecated as this service lies outside scope of specification and should not be used.
	 */
	@Deprecated
	@GetMapping("/{id}")
	public KalahGameState retrieveGame(@PathVariable("id") Long id) {
		return kalahWebService.retrieveGame(id);
	}

	/**
	 * Performs a move
	 * 
	 * @param gameId game on which to perform a move.
	 * @param pitId the id of the pit on which to start the move.
	 * @return the resulting game state following the move.
	 */
	@PutMapping("/{gameId}/pits/{pitId}")
	public PerformMoveResponseModel makeMove(
			@PathVariable("gameId") Long gameId,
			@ValidPitIndex
			@PathVariable("pitId") Integer pitId) {
		return kalahWebService.makeMove(gameId, pitId);
	}
}
