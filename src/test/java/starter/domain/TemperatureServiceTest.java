package starter.domain;

import org.junit.Before;
import org.junit.Test;
import starter.service.TemperatureService;
import starter.service.retrofit.WeatherApiManager;

import java.io.IOException;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class TemperatureServiceTest {

    TemperatureService service;
    WeatherApiManager weatherApiManager;

    @Before
    public void setUp() {
        weatherApiManager = new WeatherApiManager();
        service = new TemperatureService(weatherApiManager);
    }

    @Test
    public void getTemperatureFor_withValidCity_returnsOk() throws IOException {
        double temp = service.getTemperatureFor(LocalDate.now().plusDays(1), "Buenos Aires");
        assertThat(temp).hasNoNullFieldsOrProperties();
    }

    @Test
    public void getTemperatureFor_withInvalidDate_throwsException() {
        assertThatThrownBy(() -> service.getTemperatureFor(LocalDate.now().plusDays(10), "Buenos Aires"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Can not get temperature for a date greater than 5 days from now.");
    }
}
