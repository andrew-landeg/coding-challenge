package uk.org.landeg.kalah;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import uk.org.landeg.kalah.game.KalahGameBoard;
import uk.org.landeg.kalah.game.KalahGameBoardStandard;
import uk.org.landeg.kalah.game.KalahGameEngine;
import uk.org.landeg.kalah.game.action.KalahMoveProcessor;
import uk.org.landeg.kalah.game.action.KalahMoveProcessorImpl;

@TestConfiguration
public class CommonTestConfiguration {
	@Bean
	KalahGameEngine gameService() {
		return new KalahGameEngine();
	}

	@Bean
	KalahGameBoard gameBoard () {
		return new KalahGameBoardStandard();
	}

	@Bean
	KalahMoveProcessor moveProcessor() {
		return new KalahMoveProcessorImpl();
	}
}
