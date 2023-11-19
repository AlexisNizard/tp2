package repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import models.Hotel;
public interface HotelRepository extends JpaRepository<Hotel, Integer>{
}