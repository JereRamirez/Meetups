package starter.converters;

import starter.api.MeetupRequest;
import starter.domain.Attendee;
import starter.domain.Meetup;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public interface MeetupRequestToMeetupConverter {
    static Meetup convert(MeetupRequest request){
        List<Attendee> attendees = request.getAttendeesDni().stream()
                .map(dni -> {
                    Attendee attendee = new Attendee();
                    attendee.setDni(dni);
                    attendee.setAttended(false);
                    return attendee;
                }).collect(Collectors.toList());

        Meetup meetup = new Meetup();
        meetup.setDate(LocalDate.parse(request.getDate()));
        meetup.setCity(request.getCity());
        meetup.setAttendees(attendees);

        return meetup;
    }
}
