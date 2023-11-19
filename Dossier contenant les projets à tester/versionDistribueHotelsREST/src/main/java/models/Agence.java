package models;

import java.util.List;
import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class Agence {
	/* ATTRIBUTES */
	@Id
	private int Id;	
	private String Nom;
	
	@OneToMany(mappedBy = "agence")
	private List<EstPartenaire> partenaires;

	/* CONSTRUCTORS */
	public Agence() {
		
	}
	public Agence(int id, String nom) {
		super();
		Id = id;
		Nom = nom;
	}

	/* METHODS */
	public int getId() {
		return Id;
	}

	public void setId(int id) {
		Id = id;
	}

	public String getNom() {
		return Nom;
	}

	public void setNom(String nom) {
		Nom = nom;
	}
	
	
	public List<EstPartenaire> getPartenaires() {
		return partenaires;
	}

	public void setPartenaires(List<EstPartenaire> partenaires) {
		this.partenaires = partenaires;
	}
}

