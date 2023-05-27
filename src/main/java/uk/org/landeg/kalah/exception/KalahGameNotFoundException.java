package uk.org.landeg.kalah.exception;

/**
 * Thrown on game not found.
 * @author Andrew Landeg
 *
 */
public class KalahGameNotFoundException extends KalahClientException{

	public KalahGameNotFoundException() {
		super("the requested game does not exist.");
	}
}
