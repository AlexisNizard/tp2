package models;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
public class Hotel {
	/* ATTRIBUTES */
	@Id
	private int idHotel;
	private String nom;
	private String pays;
	private String ville;
	private String rue;
	private int categHotel;
	private double coordX;
	private double coordY;

	//On indique que c'est une relation OneToMany avec des chambres d'hôtels
	@OneToMany(mappedBy="hotel", cascade = CascadeType.ALL)
	private List<Chambre> listeChambres;
	
	@OneToMany(mappedBy = "hotel", cascade = CascadeType.ALL)
	private List<EstPartenaire> partenaires;
	
	//On indique que c'est une relation OneToMany avec des offres
	@OneToMany(mappedBy="hotel", cascade = CascadeType.ALL)
	private List<Offre> listeOffres;
	
	/* CONSTRUCTORS */
	public Hotel() {
		
	}
	
	public Hotel(int idHotel,String nom, String pays, String ville, String rue, int categHotel,
			double coordX, double coordY) {
		super();
		this.idHotel = idHotel;
		this.nom = nom;
		this.pays = pays;
		this.ville = ville;
		this.rue = rue;
		this.setCategHotel(categHotel);
		this.coordX = coordX;
		this.coordY = coordY;
	}
	
	/* METHODS */
	public int getIdHotel() {
		return idHotel;
	}

	public void setIdHotel(int idHotel) {
		this.idHotel = idHotel;
	}
	
	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public String getPays() {
		return pays;
	}

	public void setPays(String pays) {
		this.pays = pays;
	}

	public String getVille() {
		return ville;
	}

	public void setVille(String ville) {
		this.ville = ville;
	}

	public String getRue() {
		return rue;
	}

	public void setRue(String rue) {
		this.rue = rue;
	}

	public int getCategHotel() {
		return categHotel;
	}

	public void setCategHotel(int categHotel) {
		this.categHotel = categHotel;
	}

	public double getCoordX() {
		return coordX;
	}

	public void setCoordX(double coordX) {
		this.coordX = coordX;
	}

	public double getCoordY() {
		return coordY;
	}

	public void setCoordY(double coordY) {
		this.coordY = coordY;
	}

	public List<Chambre> getListeChambres() {
		return listeChambres;
	}
	
	public void setListeChambres(List<Chambre> listeChambres) {
		System.out.println("dddsds");
		try {
		this.listeChambres = listeChambres;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public List<EstPartenaire> getPartenaires() {
		return partenaires;
	}

	public void setPartenaires(List<EstPartenaire> partenaires) {
		this.partenaires = partenaires;
	}
	
	
	public List<Offre> getListeOffres() {
		return listeOffres;
	}


	public void setListeOffres(ArrayList<Offre> listeOffres) {
		this.listeOffres = listeOffres;
	}
	
	public void ajouterOffre(Offre o) {
		this.listeOffres.add(o);
	}

	@Override
	public int hashCode() {
		return Objects.hash(categHotel, coordX, coordY, idHotel, listeChambres, listeOffres, nom, pays, rue, ville);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Hotel other = (Hotel) obj;
		return categHotel == other.categHotel
				&& Double.doubleToLongBits(coordX) == Double.doubleToLongBits(other.coordX)
				&& Double.doubleToLongBits(coordY) == Double.doubleToLongBits(other.coordY) && idHotel == other.idHotel
				&& Objects.equals(listeChambres, other.listeChambres) && Objects.equals(listeOffres, other.listeOffres)
				&& Objects.equals(nom, other.nom) && Objects.equals(pays, other.pays) && Objects.equals(rue, other.rue)
				&& Objects.equals(ville, other.ville);
	}

	@Override
	public String toString() {
		return "Hotel [idHotel=" + idHotel + ", nom=" + nom + ", pays=" + pays + ", ville=" + ville + ", rue=" + rue
				+ ", categHotel=" + categHotel + ", coordX=" + coordX + ", coordY=" + coordY + ", listeChambres="
				+ listeChambres + ", listeOffres=" + listeOffres + "]";
	}
	
	

	
}
