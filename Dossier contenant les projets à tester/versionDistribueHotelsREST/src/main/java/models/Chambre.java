package models;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
public class Chambre {
	/* ATTRIBUTES */
	@Id
	private int idChambre;
	private int nbrLits;
	private float prix; //représente le prix d'une nuit dans la chambre
	
	//Représente un tableau de couple [DateDebut,DateFin] correspondant aux dates reservées	
	//On indique que c'est une relation OneToMany avec des dates reservées
	@OneToMany(mappedBy="chambre", cascade = CascadeType.ALL)
	private List<DatesReservees> datesReservees;
	
	private String nomImage;
	
	//On indique que c'est une relation ManyToOne avec un hôtel
	@ManyToOne(fetch=FetchType.LAZY)
	@JsonIgnore
	private Hotel hotel;
	
	
	/* CONSTRUCTORS */
	public Chambre() {
		
	}

	public Chambre(int idChambre, int nbrLits, float prix,String nomImage,Hotel hotel) {
		super();
		this.idChambre = idChambre;
		this.nbrLits = nbrLits;
		this.prix = prix;
		this.nomImage=nomImage;
		this.hotel=hotel;
	}
	
	/* METHODS */
	public int getIdChambre() {
		return idChambre;
	}
	public void setIdChambre(int idChambre) {
		this.idChambre = idChambre;
	}
	public int getNbrLits() {
		return nbrLits;
	}
	public void setNbrLits(int nbrLits) {
		this.nbrLits = nbrLits;
	}
	public float getPrix() {
		return prix;
	}
	public void setPrix(float prix) {
		this.prix = prix;
	}
	
	public List<DatesReservees> getDatesReservees() {
		return datesReservees;
	}

	public void setDatesReservees(List<DatesReservees> datesReservees) {
		this.datesReservees = datesReservees;
	}

	public void addUneReservation(LocalDateTime dateDebut,LocalDateTime dateFin,Chambre chambre) {
		this.datesReservees.add(new DatesReservees(dateDebut,dateFin,chambre));
	}
	
	public Hotel getHotel() {
		return hotel;
	}

	public void setHotel(Hotel hotel) {
		this.hotel = hotel;
	}

	
	
	/* L'idée derrière cette méthode est la suivante : 
     * Pour confirmer que le créneau est libre,  
     * il faut que la date de debut et de fin passé en paramètre soit (INFERIEUR A TOUTES les dates de debut) 
     * || (SUPERIEUR A TOUTES les dates de fin) présentent dans les réservations */
	public boolean estDisponible(LocalDateTime dateDebut,LocalDateTime dateFin) {
		
		for (DatesReservees tabDates : this.datesReservees) {
            
            if (  !((dateDebut.isBefore(tabDates.getDateDebut()) && dateFin.isBefore(tabDates.getDateDebut())) ||
            	  (dateDebut.isAfter(tabDates.getDateFin()) && dateFin.isAfter(tabDates.getDateFin())))  ) {
            	return false;
            }
        }
		return true;
	}

	public String getNomImage() {
		return nomImage;
	}

	public void setNomImage(String nomImage) {
		this.nomImage = nomImage;
	}

	@Override
	public String toString() {
		return "Chambre [idChambre=" + idChambre + ", nbrLits=" + nbrLits + ", prix=" + prix + ", datesReservees="
				+ datesReservees + ", nomImage=" + nomImage + ", hotel=" + hotel + "]";
	}
	
}
