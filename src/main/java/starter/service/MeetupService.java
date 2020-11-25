package starter.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import starter.controller.MeetupController;
import starter.domain.Attendee;
import starter.domain.Meetup;
import starter.repository.MeetupRepository;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MeetupService {

    @Autowired
    private MeetupRepository meetupRepository;
    @Autowired
    private TemperatureService temperatureService;
    @Autowired
    private PackCalculatorService packCalculatorService;
    private final Logger logger = LoggerFactory.getLogger(MeetupController.class);

    public Iterable<Meetup> getAll() {
        logger.info("Request received to get all Meetups.");
        return meetupRepository.findAll();
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

    public int getPacksNeeded(String id) throws IOException {
        Optional<Meetup> meetup = meetupRepository.findById(id);
        if (!meetup.isPresent()) return 0;
        logger.info("Request to get necessary packs for Meetup with id: {}.", id);
        return packCalculatorService.calculatePackBirrasNecessity(meetup.get());
    }

    public void addAttendee(String id, Attendee attendee) {
        Optional<Meetup> meetup = meetupRepository.findById(id);
        meetup.ifPresent(meet -> {
            if (meet.getDate().isAfter(LocalDate.now())) {
                logger.info("Meetup {} updated", meet.getId());
                meet.getAttendees().add(attendee);
                meetupRepository.save(meet);
            }
        });
    }

    public void checkInAttendee(String id, String dni) {
        Optional<Meetup> meetup = meetupRepository.findById(id);
        meetup.ifPresent(meet -> {
            if (meet.getDate().isBefore(LocalDate.now())) {
                List<Attendee> filteredAttendees = meet.getAttendees()
                        .stream()
                        .filter(att -> att.getDni().equals(dni))
                        .collect(Collectors.toList());

                if (!filteredAttendees.isEmpty()) {
                    filteredAttendees.get(0).setAttended(true);
                    meetupRepository.save(meet);
                    logger.info("Meetup {} updated", meet.getId());
                }
            }
        });
    }
}
