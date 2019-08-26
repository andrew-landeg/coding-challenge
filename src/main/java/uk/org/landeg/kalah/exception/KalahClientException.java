package uk.org.landeg.kalah.exception;

public class KalahClientException extends KalahException {

	public KalahClientException() {
		this("There's something wrong with your request.");
	}

	public KalahClientException(String message) {
		super(message, "CLIENT_ERROR");
	}

	private static final long serialVersionUID = 5767337950884009335L;

}
