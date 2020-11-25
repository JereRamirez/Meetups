package starter.service.retrofit;


import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ClimateInterface {

    @GET("forecast")
    Call<WeatherInfo> getTemperature(@Query("q") String city, @Query("units") String unitSystem);

}
