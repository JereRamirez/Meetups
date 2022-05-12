package starter.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import starter.domain.Attendee;
import starter.domain.Meetup;
import starter.repository.MeetupRepository;

import java.io.IOException;
import java.util.Optional;

@Service
public class MeetupService {
    private final MeetupRepository meetupRepository;
    private final TemperatureService temperatureService;
    private final PackCalculatorService packCalculatorService;
    private final Logger logger = LoggerFactory.getLogger(MeetupService.class);

    @Autowired
    public MeetupService(MeetupRepository meetupRepository, TemperatureService temperatureService, PackCalculatorService packCalculatorService) {
        this.meetupRepository = meetupRepository;
        this.temperatureService = temperatureService;
        this.packCalculatorService = packCalculatorService;
    }

    public void addMeetup(Meetup meetup) {
        logger.info("Meetup {} updated", meetup.getId());
        meetupRepository.save(meetup);
    }

    public double getTemperatureFor(String id) throws IOException {
        Optional<Meetup> meetup = meetupRepository.findById(id);
        if (!meetup.isPresent()) return 0;
        logger.info("Request to get temperature forecast for Meetup with id: {}.", id);
        return temperatureService.getTemperatureFor(meetup.get().getDate(), meetup.get().getCity());
    }

    public int getPacksNeeded(String meetupId) throws IOException {
        Optional<Meetup> meetup = meetupRepository.findById(meetupId);
        if (!meetup.isPresent()) return 0;
        logger.info("Request to get necessary packs for Meetup with id: {}.", meetupId);
        return packCalculatorService.calculatePackBirrasNecessity(meetup.get());
    }

    public void addAttendee(String meetupId, Attendee attendee) {
        Optional<Meetup> meetup = meetupRepository.findById(meetupId);
        meetup.ifPresent(meet -> {
            meet.addAttendee(attendee);
            meetupRepository.save(meet);
            logger.info("Meetup {} updated", meet.getId());
        });
    }

    public void checkInAttendee(String meetupId, String dni) {
        Optional<Meetup> meetup = meetupRepository.findById(meetupId);
        meetup.ifPresent(meet -> {
            meet.checkInAttendee(dni);
            meetupRepository.save(meet);
            logger.info("Attendee with dni: {} for Meetup: {} updated", dni, meet.getId());
        });
    }
}
