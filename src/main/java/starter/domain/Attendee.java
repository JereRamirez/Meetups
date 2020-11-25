package starter.domain;

import lombok.Data;

import javax.persistence.Embeddable;

@Embeddable
@Data
public class Attendee {

    private String dni;
    private Boolean attended;
}
