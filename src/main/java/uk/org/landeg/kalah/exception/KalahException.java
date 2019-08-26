package uk.org.landeg.kalah.exception;

public class KalahException extends RuntimeException {
	private final String type;

	public KalahException(String message) {
		this(message, "SERVER_ERROR");
	}

	public KalahException(String message, String type) {
		super(message);
		this.type = type;
	}
	
	public String getType() {
		return type;
	}

	private static final long serialVersionUID = -3080322494968413507L;

}
