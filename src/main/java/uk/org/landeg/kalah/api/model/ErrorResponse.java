package uk.org.landeg.kalah.api.model;

import lombok.Getter;
import uk.org.landeg.kalah.exception.KalahException;

/**
 * REST Error Model object.
 * 
 * @author Andrew Landeg
 *
 */

@Getter
public class ErrorResponse {
	private final String message;

	private final String type;

	public ErrorResponse(KalahException ex) {
		this.message = ex.getMessage();
		this.type = ex.getType();
	}
}
