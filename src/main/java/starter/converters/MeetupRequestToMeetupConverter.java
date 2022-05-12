package starter.converters;

import starter.api.MeetupRequest;
import starter.domain.Attendee;
import starter.domain.Meetup;

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

        return Meetup.builder()
                .date(request.getDate())
                .city(request.getCity())
                .attendees(attendees).build();
    }
}
