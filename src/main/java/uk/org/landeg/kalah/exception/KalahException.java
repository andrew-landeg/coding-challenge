package uk.org.landeg.kalah.exception;

import lombok.Getter;

public class KalahException extends RuntimeException {
	@Getter
	private final String type;

	public KalahException(String message) {
		this(message, "SERVER_ERROR");
	}

	public KalahException(String message, String type) {
		super(message);
		this.type = type;
	}
}
