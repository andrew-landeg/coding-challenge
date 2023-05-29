package uk.org.landeg.kalah.api;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import uk.org.landeg.kalah.api.model.ErrorResponse;
import uk.org.landeg.kalah.exception.KalahClientException;
import uk.org.landeg.kalah.exception.KalahException;
import uk.org.landeg.kalah.exception.KalahGameNotFoundException;

@Slf4j
@RestControllerAdvice
public class ErrorAdvisor {
	@Order(100)
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

	@ResponseStatus(code=HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(KalahException.class)
	public ErrorResponse handleKalahException(KalahException ex) {
		log.error("SERVER ERROR", ex);
		return new ErrorResponse(ex);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handleOtherException(Exception ex) {
		//TODO - define custom ExceptionHandlerExceptionResolver to handle this scenario.
		if (ex.getCause() instanceof KalahClientException) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new ErrorResponse(new KalahException(ex.getMessage(), "CLIENT ERROR")));
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.body(new ErrorResponse(new KalahException(ex.getMessage(), "SERVER_ERROR")));
	}
}
