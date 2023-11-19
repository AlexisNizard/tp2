package exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class WrongIdOrPasswordException extends RuntimeException {
	public WrongIdOrPasswordException(){
		
	}
	
	public WrongIdOrPasswordException(String message) {
		super(message);
	}
}