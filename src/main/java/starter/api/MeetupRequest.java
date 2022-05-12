package starter.api;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class MeetupRequest {
    private LocalDate date;
    private String city;
    private List<String> attendeesDni;
}
