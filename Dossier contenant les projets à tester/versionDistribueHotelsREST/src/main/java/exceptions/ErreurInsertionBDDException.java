package exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class ErreurInsertionBDDException extends RuntimeException {
	public ErreurInsertionBDDException(){
		
	}
	
	public ErreurInsertionBDDException(String message) {
		super(message);
	}
}