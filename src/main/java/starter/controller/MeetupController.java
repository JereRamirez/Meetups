package starter.controller;

import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;
import starter.api.MeetupRequest;
import starter.converters.MeetupRequestToMeetupConverter;
import starter.domain.Attendee;
import starter.domain.Meetup;
import starter.service.MeetupService;

import java.io.IOException;

@RestController
public class MeetupController {
    private final MeetupService meetupService;
    private final Logger logger = LoggerFactory.getLogger(MeetupController.class);

    @Autowired
    public MeetupController(MeetupService meetupService) {
        this.meetupService = meetupService;
    }

    @Cacheable("temperatures")
    @GetMapping("/meetups/{id}/temperature")
    @ApiOperation(value = "Get Meetup's temperature",
            notes = "Provide meetup's id to get its forecast temperature")
    public double getMeetupTemperature(@PathVariable String id) throws IOException {
        logger.info("Getting temperature info for MeetUp: {}", id);
        return meetupService.getTemperatureFor(id);
    }

    @GetMapping("/meetups/{id}/packBirras")
    @ApiOperation(value = "Calculate packs needed for a Meetup",
            notes = "Provide meetup's id to get packs needed. Available for ADMINs")
    public int getPacksNeeded(@PathVariable String id) throws IOException {
        return meetupService.getPacksNeeded(id);
    }

    @PostMapping("/meetups")
    @ApiOperation(value = "Add a Meetup",
            notes = "Add a Meetup to the database. Available for ADMINs")
    public void addMeetup(@RequestBody MeetupRequest meetupRequest) {
        meetupService.addMeetup(MeetupRequestToMeetupConverter.convert(meetupRequest));
    }

    @PutMapping("/meetups/{id}")
    @ApiOperation(value = "Add user to a Meetup",
            notes = "Provide a Meetup id and an Attendee DNI to add to that Meetup. Available for USERs")
    public void addAttendee(@PathVariable String id, @RequestBody Attendee attendee) {
        meetupService.addAttendee(id, attendee);
    }

    @PatchMapping("/meetups/{id}")
    @ApiOperation(value = "Chek-in user to a Meetup",
            notes = "Provide a Meetup id and an Attendee DNI to mark to that Meetup. Available for USERs")
    public void checkInAttendee(@PathVariable String id, @RequestBody String attendeeDni) {
        meetupService.checkInAttendee(id, attendeeDni);
    }
}



