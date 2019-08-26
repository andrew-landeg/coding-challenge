package uk.org.landeg.kalah.api.model;

import uk.org.landeg.kalah.exception.KalahException;

/**
 * REST Error Model object.
 * 
 * @author Andrew Landeg
 *
 */

public class ErrorResponse {
	private final String message;

	private final String type;

	public ErrorResponse(KalahException ex) {
		this.message = ex.getMessage();
		this.type = ex.getType();
	}
	
	public String getMessage() {
		return message;
	}
	
	public String getType() {
		return type;
	}
	
}
