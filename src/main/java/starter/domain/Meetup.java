package starter.domain;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

@Entity
@Data
public class Meetup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;
    private LocalDate date;
    private String city;
    @ElementCollection
    private List<Attendee> attendees = new LinkedList<>();

    public void addAttendee(Attendee newAttendee){
        if(date.isAfter(LocalDate.now())){
            attendees.add(newAttendee);
        }
    }

    public void checkInAttendee(String attendeeDni){
        if(date.isBefore(LocalDate.now())){
            attendees.stream().filter(attendee -> attendee.getDni().equalsIgnoreCase(attendeeDni))
                    .findFirst().ifPresent(Attendee::attendMeet);
        }
    }
}
