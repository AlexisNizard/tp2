package models;

public class ReserveDTO {
	/* ATTRIBUTES */
	private int idAgence;
	private String mdpAgence;
	private int idOffre;
	private String nomClient;
	private String prenomClient;
	private String codeCB;
	
	/* CONSTRUCTORS */
	public ReserveDTO(int idAgence, String mdpAgence, int idOffre, String nomClient, String prenomClient,
			String codeCB) {
		super();
		this.idAgence = idAgence;
		this.mdpAgence = mdpAgence;
		this.idOffre = idOffre;
		this.nomClient = nomClient;
		this.prenomClient = prenomClient;
		this.codeCB = codeCB;
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

	public int getIdOffre() {
		return idOffre;
	}

	public void setIdOffre(int idOffre) {
		this.idOffre = idOffre;
	}

	public String getNomClient() {
		return nomClient;
	}

	public void setNomClient(String nomClient) {
		this.nomClient = nomClient;
	}

	public String getPrenomClient() {
		return prenomClient;
	}

	public void setPrenomClient(String prenomClient) {
		this.prenomClient = prenomClient;
	}

	public String getCodeCB() {
		return codeCB;
	}

	public void setCodeCB(String codeCB) {
		this.codeCB = codeCB;
	}
	
	
	
	
}
