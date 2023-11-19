package repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import models.Chambre;
public interface ChambreRepository extends JpaRepository<Chambre, Integer>{
}
