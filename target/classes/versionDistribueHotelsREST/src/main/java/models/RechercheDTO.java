package models;

public class RechercheDTO {
	
	/* ATTRIBUTES */
	private int idAgence;
	private String mdpAgence;
	private String dateArrive;
	private String dateDepart;
	private int nbrPersonne;
	
	/* CONSTRUCTORS */
	public RechercheDTO() {
		
	}
	public RechercheDTO(int idAgence, String mdpAgence, String dateArrive, String dateDepart, int nbrPersonne) {
		super();
		this.idAgence = idAgence;
		this.mdpAgence = mdpAgence;
		this.dateArrive = dateArrive;
		this.dateDepart = dateDepart;
		this.nbrPersonne = nbrPersonne;
	}
	
	/* METHODS */
	public int getIdAgence() {
		return idAgence;
	}
	public void setIdAgence(int idAgence) {
		this.idAgence = idAgence;
	}
	public String getMdpAgence() {
		return mdpAgence;
	}
	public void setMdpAgence(String mdpAgence) {
		this.mdpAgence = mdpAgence;
	}
	public String getDateArrive() {
		return dateArrive;
	}
	public void setDateArrive(String dateArrive) {
		this.dateArrive = dateArrive;
	}
	public String getDateDepart() {
		return dateDepart;
	}
	public void setDateDepart(String dateDepart) {
		this.dateDepart = dateDepart;
	}
	public int getNbrPersonne() {
		return nbrPersonne;
	}
	public void setNbrPersonne(int nbrPersonne) {
		this.nbrPersonne = nbrPersonne;
	}
	
	

}
