package starter.service;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import retrofit2.Response;
import starter.service.retrofit.TemperatureInfos;
import starter.service.retrofit.WeatherApiManager;
import starter.service.retrofit.WeatherInfo;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Data
@RequiredArgsConstructor
public class TemperatureService {
    private WeatherApiManager weatherApiManager;
    private final Logger logger = LoggerFactory.getLogger(TemperatureService.class);

    @Autowired
    public TemperatureService(WeatherApiManager manager) {
        this.weatherApiManager = manager;
    }

    public double getTemperatureFor(LocalDate date, String city) throws IOException {
        if (!isValidDate(date))
            throw new IllegalArgumentException("Can not get temperature for a date greater than 5 days from now.");

        Response<WeatherInfo> temperatureInfo = weatherApiManager.getTemperature(city.concat(", ar"));

        return getTemperatureFor(temperatureInfo, date);
    }

    private double getTemperatureFor(Response<WeatherInfo> temperatureInfo, LocalDate date) {
        assert temperatureInfo.body() != null;
        List<TemperatureInfos> tempByHour = temperatureInfo.body().getList();

        Optional<TemperatureInfos> temperature = tempByHour.stream()
                .filter(climateInfo -> isWantedDate(climateInfo.getDt_txt(), date))
                .findFirst();
        double forecast = 0.;

        if(temperature.isPresent())
            forecast = temperature.get().getMain().getTemp();

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
