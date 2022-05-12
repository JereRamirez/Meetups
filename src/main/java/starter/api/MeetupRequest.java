package starter.api;

import lombok.Data;

import java.util.List;

@Data
public class MeetupRequest {
    private String date;
    private String city;
    private List<String> attendeesDni;
}
