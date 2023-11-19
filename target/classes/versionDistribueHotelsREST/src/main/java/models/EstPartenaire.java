package models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class EstPartenaire {
	
	/* ATTRIBUTES */
	@Id
	@GeneratedValue
	private int IdEstPartenaire;
	@ManyToOne(fetch=FetchType.LAZY)
	@JsonIgnore
	private Agence agence;
	@ManyToOne(fetch=FetchType.LAZY)
	@JsonIgnore
	private Hotel hotel;
	
	private String mdp;

	/* CONSTRUCTORS */
	public EstPartenaire() {
		
	}
	public EstPartenaire(Agence agence, String mdp, Hotel hotel) {
		super();
		this.agence = agence;
		this.mdp = mdp;
		this.hotel = hotel;
	}
	
	/* METHODS */
	public Agence getAgence() {
		return agence;
	}
	public void setAgence(Agence agence) {
		this.agence = agence;
	}
	public Hotel getHotel() {
		return hotel;
	}
	public void setHotel(Hotel hotel) {
		this.hotel = hotel;
	}
	public String getMdp() {
		return mdp;
	}
	public void setMdp(String mdp) {
		this.mdp = mdp;
	}
	public int getIdEstPartenaire() {
		return IdEstPartenaire;
	}

	public void setIdEstPartenaire(int idEstPartenaire) {
		IdEstPartenaire = idEstPartenaire;
	}

}
