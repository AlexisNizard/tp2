package exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class ErreurRecuperationHotelBDDException extends RuntimeException {
	public ErreurRecuperationHotelBDDException(){
		
	}

	public ErreurRecuperationHotelBDDException(String message) {
		super(message);
	}
}