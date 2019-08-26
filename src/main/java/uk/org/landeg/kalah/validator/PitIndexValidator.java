package uk.org.landeg.kalah.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import uk.org.landeg.kalah.exception.KalahClientException;
import uk.org.landeg.kalah.game.KalahGameBoard;
import uk.org.landeg.kalah.game.KalahGameBoardStandard;

public class PitIndexValidator implements ConstraintValidator<ValidPitIndex, Integer>{
	final KalahGameBoard board = KalahGameBoardStandard.INSTANCE;

	@Override
	public boolean isValid(Integer value, ConstraintValidatorContext context) {
		if (!board.getAllPlayerPits().contains(value)) {
			throw new KalahClientException("Invalid pit number, only values 1-6 or 7-13 are allowed");
		}
		return true;
	}
}
