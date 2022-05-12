package starter.domain;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import starter.service.PackCalculatorService;
import starter.service.TemperatureService;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class PackCalculatorUtilsTest {

    private PackCalculatorService calculator;
    private TemperatureService temperatureService;

    @Before
    public void setUp() {
        temperatureService = Mockito.mock(TemperatureService.class);
        calculator = new PackCalculatorService(temperatureService);
    }

    @Test
    public void calculatePackBirrasNecessity_emptyMeetup_throwsIllegalArgumentException() {
        assertThatThrownBy(() -> calculator.calculatePackBirrasNecessity(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("The Meetup can not be empty.");
    }

    @Test
    public void calculatePackBirrasNecessity_meetupWithoutAttendees_returnZero() throws IOException {
        Meetup meetup = new Meetup();
        meetup.setCity("Buenos Aires, AR");

        assertThat(calculator.calculatePackBirrasNecessity(meetup))
                .isZero();
    }

    @Test
    public void calculatePackBirrasNecessity_meetupWithoutCity_throwsIllegalArgumentException() {
        Meetup meetup = new Meetup();
        meetup.setDate(LocalDate.now());
        assertThatThrownBy(() -> calculator.calculatePackBirrasNecessity(meetup))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("The Meetup needs to have a city.");
    }

    @Test
    public void calculatePackBirrasNecessity_climateBelow20Degrees_returnOk() throws IOException {
        LocalDate date = LocalDate.now();
        String city = "Buenos Aires, AR";
        Meetup meetup = initializeMeetup(date, city);

        Mockito.when(temperatureService.getTemperatureFor(date, city)).thenReturn(19.0);

        assertCombinations(meetup, 9, 17);
    }

    @Test
    public void calculatePackBirrasNecessity_climateBetween20And24Degrees_returnOk() throws IOException {
        LocalDate date = LocalDate.now();
        String city = "Buenos Aires, AR";
        Meetup meetup = initializeMeetup(date, city);

        Mockito.when(temperatureService.getTemperatureFor(date, city)).thenReturn(20.0);

        assertCombinations(meetup, 7, 13);

        Mockito.when(temperatureService.getTemperatureFor(date, city)).thenReturn(24.0);

        assertCombinations(meetup, 7, 13);
    }

    @Test
    public void calculatePackBirrasNecessity_climateGreaterThan24Degrees_returnOk() throws IOException {
        LocalDate date = LocalDate.now();
        String city = "Buenos Aires, AR";
        Meetup meetup = initializeMeetup(date, city);

        Mockito.when(temperatureService.getTemperatureFor(date, city)).thenReturn(25.0);

        assertCombinations(meetup, 3, 5);
    }

    private Meetup initializeMeetup(LocalDate date, String city) {
        Meetup meetup = new Meetup();
        meetup.setDate(date);
        meetup.setCity(city);
        return meetup;
    }

    private void assertCombinations(Meetup meetup, int secondAttendeesSize, int thirdAttendeesSize) throws IOException {
        meetup.setAttendees(getAmountOfAttendees(1));

        assertThat(calculator.calculatePackBirrasNecessity(meetup))
                .isEqualTo(1);

        meetup.setAttendees(getAmountOfAttendees(secondAttendeesSize));

        assertThat(calculator.calculatePackBirrasNecessity(meetup))
                .isEqualTo(2);

        meetup.setAttendees(getAmountOfAttendees(thirdAttendeesSize));

        assertThat(calculator.calculatePackBirrasNecessity(meetup))
                .isEqualTo(3);
    }

    private List<Attendee> getAmountOfAttendees(int count) {
        return Collections.nCopies(count, new Attendee());
    }

}
