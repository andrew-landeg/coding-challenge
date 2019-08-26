package uk.org.landeg.kalah.api;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import uk.org.landeg.kalah.api.model.ErrorResponse;
import uk.org.landeg.kalah.exception.KalahClientException;
import uk.org.landeg.kalah.exception.KalahGameNotFoundException;

@RestControllerAdvice
public class ErrorAdvisor {
	@ResponseStatus(code=HttpStatus.NOT_FOUND)
	@ExceptionHandler(KalahGameNotFoundException.class)
	public ErrorResponse handleKalahClientException(KalahGameNotFoundException ex) {
		return new ErrorResponse(ex);
	}

	@ResponseStatus(code=HttpStatus.BAD_REQUEST)
	@ExceptionHandler(KalahClientException.class)
	public ErrorResponse handleKalahClientException(KalahClientException ex) {
		return new ErrorResponse(ex);
	}
}
