package by.sergo.cab.passengerservice.repository;

import by.sergo.cab.passengerservice.domain.entity.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Long> {

    List<Rating> getRatingsByPassengerId(Long passengerId);
}
