package exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class AucuneOffreTrouveeException extends RuntimeException {
	public AucuneOffreTrouveeException(){
		
	}
	
	public AucuneOffreTrouveeException(String message) {
		super(message);
	}
}