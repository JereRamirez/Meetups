package starter.service;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import starter.controller.MeetupController;
import starter.domain.Meetup;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Objects;

@Service
@Data
@RequiredArgsConstructor
public class PackCalculatorService {

    @Autowired
    @NonNull
    private TemperatureService service;
    private final Logger logger = LoggerFactory.getLogger(MeetupController.class);

    public int calculatePackBirrasNecessity(Meetup meetup) throws IOException {
        validateMeetup(meetup);

        if (Objects.isNull(meetup.getAttendees())) {
            return 0;
        }

        double climateFactor = getClimateFactor(meetup.getDate(), meetup.getCity());

        logger.info("Climate factor based on the temperature: {}", climateFactor);
        return calculatePacksNeeded(meetup.getAttendees().size(), climateFactor);
    }

    private void validateMeetup(Meetup meetup) {
        if (Objects.isNull(meetup)) {
            throw new IllegalArgumentException("The Meetup can not be empty.");
        }

        if (Objects.isNull(meetup.getCity())) {
            throw new IllegalArgumentException("The Meetup needs to have a city.");
        }
    }

    private int calculatePacksNeeded(int attendees, double climateFactor) {
        int birrasPerPack = 6;
        int necessaryPacks = (int) Math.ceil((attendees * climateFactor) / birrasPerPack);
        logger.info("Necessary packs for the Meetup: {}", necessaryPacks);
        return necessaryPacks;
    }

    private double getClimateFactor(LocalDate date, String city) throws IOException {
        double temperature = service.getTemperatureFor(date, city);
        return decideFactorByTemp(temperature);
    }

    private double decideFactorByTemp(double temperature) {
        double x;
        if (temperature < 20) {
            x = 0.75;
        } else {
            if (temperature <= 24) {
                x = 1;
            } else {
                x = 3;
            }
        }
        return x;
    }
}
