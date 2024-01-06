package by.sergo.rideservice.repository;

import by.sergo.rideservice.domain.Ride;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RideRepository extends MongoRepository<Ride, String> {
}
