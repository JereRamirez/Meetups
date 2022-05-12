package starter.domain;


import lombok.Data;

import javax.persistence.Embeddable;

@Data
@Embeddable
public class Attendee {

    private String dni;
    private Boolean attended;

    public void attendMeet() { this.attended = true; }


}
