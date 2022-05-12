package starter.domain;

import org.junit.Before;
import org.junit.Test;
import retrofit2.Call;
import retrofit2.Response;
import starter.service.retrofit.ClimateInterface;
import starter.service.retrofit.WeatherApiManager;
import starter.service.retrofit.WeatherInfo;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;


public class WeatherApiTest {

    ClimateInterface service;

    @Before
    public void setUp() {
        service = WeatherApiManager.createService(ClimateInterface.class);
    }

    @Test
    public void getTemperature_validCity_returnOk() throws IOException {
        Call<WeatherInfo> weatherInfo = service.getTemperature("buenos aires, ar", "metric");
        Response<WeatherInfo> response = weatherInfo.execute();

        assertThat(response.body())
                .hasNoNullFieldsOrProperties()
                .isNotNull();
    }

    @Test
    public void getTemperature_invalidParameters_returnOk() throws IOException {
        Call<WeatherInfo> weatherInfo = service.getTemperature(null, "metric");
        Response<WeatherInfo> response = weatherInfo.execute();

        assertThat(response.code())
                .isEqualTo(400);
    }
}
