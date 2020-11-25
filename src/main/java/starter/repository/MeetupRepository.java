package starter.repository;

import org.springframework.data.repository.CrudRepository;
import starter.domain.Meetup;

public interface MeetupRepository extends CrudRepository<Meetup, String> {

}
