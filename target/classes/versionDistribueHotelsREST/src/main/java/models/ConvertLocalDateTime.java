package models;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ConvertLocalDateTime {
	
	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
	
	public String DateToString(LocalDateTime l) {
		return l.format(formatter); 
	}
	
	public LocalDateTime StringToDate(String s) {
		return LocalDateTime.parse(s, formatter);
	}
}
