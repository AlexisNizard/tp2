package controllers;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import exceptions.AucuneOffreTrouveeException;
import exceptions.ErreurRecuperationHotelBDDException;
import exceptions.ErreurRecuperationOffreBDDException;
import exceptions.ErreurReservationException;
import exceptions.WrongIdOrPasswordException;
import models.Chambre;
import models.ConvertImage;
import models.ConvertLocalDateTime;
import models.DatesReservees;
import models.EstPartenaire;
import models.Hotel;
import models.Offre;
import models.RechercheDTO;
import models.ReserveDTO;
import repositories.AgenceRepository;
import repositories.ChambreRepository;
import repositories.DatesReserveesRepository;
import repositories.EstPartenaireRepository;
import repositories.HotelRepository;
import repositories.OffreRepository;

@RestController
public class HotelsController {
	/* ATTRIBUTES */	
	private static final String uri = "hotel/api";
	
	@Autowired
	private HotelRepository hotelRepository;	 
	@Autowired
	private ChambreRepository chambreRepository;	
	@Autowired
	private DatesReserveesRepository datesReserveesRepository;
	@Autowired
	private OffreRepository  offreRepository;
	@Autowired
	private EstPartenaireRepository estPartenaireRepository;
	

	/* METHODS */
	public boolean verifConnexion(int idAgence, String mdpAgence,Hotel hotel) throws WrongIdOrPasswordException {
		
		for (EstPartenaire p : estPartenaireRepository.findAll()) {
			if (p.getHotel().getIdHotel() == hotel.getIdHotel()) {
				if (p.getAgence().getId() == idAgence && p.getMdp().equals(mdpAgence)) {
					return true;					
				}
			}
		}
		throw new WrongIdOrPasswordException("L'identifiant ou le mot de passe n'est pas reconnu");	
		
	}
	
	
	/* METHODS REST*/		
	@ResponseStatus(HttpStatus.ACCEPTED)
	@PostMapping(uri+"/recherche")
	public List<Offre> recherche(@RequestBody RechercheDTO params)
			throws WrongIdOrPasswordException,AucuneOffreTrouveeException,IOException {
		
		ConvertLocalDateTime pDT = new ConvertLocalDateTime();
		ConvertImage cI = new ConvertImage();
		Hotel hotel = hotelRepository.findById(0).orElseThrow(() -> new ErreurRecuperationHotelBDDException(
				"Erreur venant du serveur : L'hôtel n'a pas été trouvé dans la BDD "));
			
		verifConnexion(params.getIdAgence(),params.getMdpAgence(),hotel);
		List<Offre> offresRenvoyees = new ArrayList<Offre>();
		
		for (Chambre c : hotel.getListeChambres()) {//Pour chaque chambre de l'hotel
				
			//Verif nombre de lits (on renvoit egalement la chambre si il y a deux personnes et 1 lits)	
			if ((params.getNbrPersonne() == 2 && c.getNbrLits()<= 2) || (params.getNbrPersonne() == c.getNbrLits())) {
				
				if (c.estDisponible(pDT.StringToDate(params.getDateArrive()),pDT.StringToDate(params.getDateDepart()))) {//Verif disponibilitées horaires
							
					Offre offre=new Offre(c.getIdChambre(),c.getNbrLits(),params.getDateArrive(),
							params.getDateDepart(),c.getPrix(),params.getIdAgence(),cI.ImageToString(c.getNomImage()),hotel);
					
					//Ajout de l'offre au tableau que la méthode va renvoyé	
					offresRenvoyees.add(offre);	
					//Ajout de l'offre dans la BDD pour les données persistantes
					offreRepository.save(offre);

				}										
			}				
		}		
		
		if (!offresRenvoyees.isEmpty()) {
			return offresRenvoyees;
		}
		throw new AucuneOffreTrouveeException("Aucune offre ne correspond à vos critères");
		
	}
	
	
	
	@ResponseStatus(HttpStatus.ACCEPTED)
	@PostMapping(uri+"/reserve")
	public String reserve(@RequestBody ReserveDTO params) throws
	WrongIdOrPasswordException,ErreurReservationException {
		
		ConvertLocalDateTime pDT = new ConvertLocalDateTime();
		Hotel hotel = hotelRepository.findById(0).orElseThrow(() -> new ErreurRecuperationHotelBDDException(
				"Erreur venant du serveur : L'hôtel n'a pas été trouvé dans la BDD "));
		
		verifConnexion(params.getIdAgence(),params.getMdpAgence(),hotel);
		
		//Creation de la réference
		Random rand = new Random();
		int r;
		String reference="";
		for(int i=0;i<6;i++) {			
			r=rand.nextInt(10);
			reference+=Integer.toString(r);		
		}
		
		Offre offre = offreRepository.findById(params.getIdOffre()).orElseThrow(() -> new ErreurRecuperationOffreBDDException(
				"Erreur : L'offre n'a pas été trouvée dans la BDD "));
		
		for(Chambre c : chambreRepository.findAll()) {
			if (c.getIdChambre() == offre.getNumeroChambre()) {
				
				//Ajout des dates réservées dans le tableau dateReservee de la chambre
				datesReserveesRepository.save(new DatesReservees(pDT.StringToDate(offre.getDateArrivee()),pDT.StringToDate(offre.getDateDepart()),c));
				
				//Note : on pourrait également utiliser ce code à la place :
				/*	
				 * 	chambreRepository.findAll().get(c.getIdChambre()).addUneReservation(
					pDT.StringToDate(offre.getDateArrivee()), 
					pDT.StringToDate(offre.getDateDepart()),
					c
					);
				*/
				
				//Envoi de la référence
				return reference;
			}
		}
		
		throw new ErreurReservationException("Erreur lors de l'envoie de la référence");	
		
		
			
	}
	
}
