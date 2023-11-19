package repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import models.Agence;

public interface AgenceRepository extends JpaRepository<Agence, Integer>{
}
