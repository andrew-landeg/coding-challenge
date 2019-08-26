package uk.org.landeg.kalah.exception;

/**
 * Thrown on game not found.
 * @author Andrew Landeg
 *
 */
public class KalahGameNotFoundException extends KalahClientException{
	private static final long serialVersionUID = -8111960362366999952L;

	public KalahGameNotFoundException() {
		super("the requested game does not exist.");
	}
}
