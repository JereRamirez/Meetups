package starter.service.retrofit;

import lombok.Data;

import java.util.List;

@Data
public class WeatherInfo {
    private List<TemperatureInfos> list;
}
