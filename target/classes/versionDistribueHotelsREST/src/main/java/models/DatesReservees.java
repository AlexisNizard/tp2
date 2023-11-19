package models;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class DatesReservees {
	/* ATTRIBUTES */	
	@Id
	@GeneratedValue
	private int idDatesReservees;
	
	@Column(columnDefinition = "TIMESTAMP")
	private LocalDateTime dateDebut;
	@Column(columnDefinition = "TIMESTAMP")
	private LocalDateTime dateFin;
	
	
	//On indique que c'est une relation ManyToOne avec une chambre
	@ManyToOne(fetch=FetchType.LAZY)
	@JsonIgnore
	private Chambre chambre;
	
	/* CONSTRUCTORS */	
	public DatesReservees() {
	
	}
	public DatesReservees(LocalDateTime dateDebut, LocalDateTime dateFin,Chambre chambre) {
		super();
		this.dateDebut = dateDebut;
		this.dateFin = dateFin;
		this.chambre=chambre;
	}
	
	/* METHODS */	
	public int getIdDatesReservees() {
		return idDatesReservees;
	}

	public void setIdDatesReservees(int idDatesReservees) {
		this.idDatesReservees = idDatesReservees;
	}
	
	public LocalDateTime getDateDebut() {
		return dateDebut;
	}
	public void setDateDebut(LocalDateTime dateDebut) {
		this.dateDebut = dateDebut;
	}
	public LocalDateTime getDateFin() {
		return dateFin;
	}
	public void setDateFin(LocalDateTime dateFin) {
		this.dateFin = dateFin;
	}

	public Chambre getChambre() {
		return chambre;
	}

	public void setChambre(Chambre chambre) {
		this.chambre = chambre;
	}

	
}
