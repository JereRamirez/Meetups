package starter.service;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import retrofit2.Response;
import starter.controller.MeetupController;
import starter.service.retrofit.List;
import starter.service.retrofit.WeatherApiManager;
import starter.service.retrofit.WeatherInfo;

import java.io.IOException;
import java.time.LocalDate;
import java.util.stream.Collectors;

@Service
@Data
@RequiredArgsConstructor
public class TemperatureService {

    @Autowired
    private WeatherApiManager manager;
    private final Logger logger = LoggerFactory.getLogger(MeetupController.class);

    public TemperatureService(WeatherApiManager manager) {
        this.manager = manager;
    }

    public double getTemperatureFor(LocalDate date, String city) throws IOException {
        if (!isValidDate(date)) {
            throw new IllegalArgumentException("Can not get temperature for a date greater than 5 days from now.");
        }

        Response<WeatherInfo> temperatureInfo = manager.getTemperature(city.concat(", ar"));

        return getTemperatureFor(temperatureInfo, date);
    }

    private double getTemperatureFor(Response<WeatherInfo> temperatureInfo, LocalDate date) {
        java.util.List<List> tempByHour = temperatureInfo.body().getList();

        java.util.List<List> filteredHours = tempByHour.stream()
                .filter(climateInfo -> isWantedDate(climateInfo.getDt_txt(), date))
                .collect(Collectors.toList());
        //Possible upgrade: could get an avg or closest to the meetup time instead of the first
        Double forecast = filteredHours.get(0).getMain().getTemp();
        logger.info("Temperature obtained for the Meetup date: {}", forecast);
        return forecast;
    }

    private boolean isWantedDate(String apiDate, LocalDate meetDate) {
        return apiDate.startsWith("2020-10-".concat(String.valueOf(meetDate.getDayOfMonth())));
    }

    private boolean isValidDate(LocalDate date) {
        return date.isBefore(LocalDate.now().plusDays(5));
    }
}
