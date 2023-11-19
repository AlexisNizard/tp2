package models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity
public class Offre {
	
	/* ATTRIBUTES */
	
	private static int instanceCounter = 0;	
	
	@Id
	private int idOffre = 0;	
	
	@JsonIgnore
	private int numeroChambre;

	private int nbrLits;
	private String dateArrivee;
	private String dateDepart;
	private float prix;
	
	@JsonIgnore
	private int idAgence;
	
	@Transient
	private byte[] imgEncode;
	
	//On indique que c'est une relation ManyToOne avec un hôtel
	@ManyToOne(fetch=FetchType.LAZY)
	@JsonIgnore
	private Hotel hotel;
	
	/* CONSTRUCTORS*/
	public Offre() {
		instanceCounter++;
		idOffre = instanceCounter;		
	}
	
	public Offre(int numeroChambre, int nbrLits, String dateArrivee, String dateDepart, float prix,int idAgence,byte[] imgEncode,Hotel hotel) {
		super();
		this.numeroChambre = numeroChambre;
		this.nbrLits = nbrLits;
		this.dateArrivee = dateArrivee;
		this.dateDepart = dateDepart;
		this.prix = prix;
		this.idAgence=idAgence;
		this.setImgEncode(imgEncode);
		this.hotel=hotel;
		
		instanceCounter++;
		idOffre = instanceCounter;
		
	}
	
	/* METHODS */
	public int getIdOffre() {
		return idOffre;
	}

	public void setIdOffre(int idOffre) {
		this.idOffre = idOffre;
	}
	
	public int getNumeroChambre() {
		return numeroChambre;
	}
	public void setNumeroChambre(int idOffre) {
		this.numeroChambre = idOffre;
	}
	public int getNbrLits() {
		return nbrLits;
	}
	public void setNbrLits(int nbrLits) {
		this.nbrLits = nbrLits;
	}
	public String getDateArrivee() {
		return dateArrivee;
	}
	public void setDateArrivee(String dateArrivee) {
		this.dateArrivee = dateArrivee;
	}
	public String getDateDepart() {
		return dateDepart;
	}
	public void setDateDepart(String dateDepart) {
		this.dateDepart = dateDepart;
	}
	public float getPrix() {
		return this.prix;
	}
	public void setPrix(float prix) {
		this.prix = prix;
	}

	public int getIdAgence() {
		return idAgence;
	}

	public void setIdAgence(int idAgence) {
		this.idAgence = idAgence;
	}

	public byte[] getImgEncode() {
		return imgEncode;
	}

	public void setImgEncode(byte[] imgEncode) {
		this.imgEncode = imgEncode;
	}
	
	
	public Hotel getHotel() {
		return hotel;
	}

	public void setHotel(Hotel hotel) {
		this.hotel = hotel;
	}

	
}
