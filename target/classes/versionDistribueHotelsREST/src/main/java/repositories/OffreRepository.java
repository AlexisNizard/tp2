package repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import models.Offre;
public interface OffreRepository extends JpaRepository<Offre, Integer>{
}