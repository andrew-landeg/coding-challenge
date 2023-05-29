package uk.org.landeg.kalah;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import uk.org.landeg.kalah.game.KalahGameBoard;
import uk.org.landeg.kalah.game.KalahGameBoardStandard;
import uk.org.landeg.kalah.game.KalahGameEngine;
import uk.org.landeg.kalah.game.action.KalahAction;
import uk.org.landeg.kalah.game.action.KalahMoveProcessor;
import uk.org.landeg.kalah.game.action.KalahMoveProcessorImpl;

import java.util.List;

@TestConfiguration
public class CommonTestConfiguration {

	@Bean
	@Autowired
	KalahGameEngine gameService(KalahGameBoard gameBoard, List<KalahAction> kalahActions, KalahMoveProcessor moveProcessor) {
		return new KalahGameEngine(gameBoard, kalahActions, moveProcessor);
	}

	@Bean
	KalahGameBoard gameBoard () {
		return new KalahGameBoardStandard();
	}

	@Bean
	@Autowired
	KalahMoveProcessor moveProcessor(KalahGameBoard kalahGameBoard) {
		return new KalahMoveProcessorImpl(kalahGameBoard);
	}
}
