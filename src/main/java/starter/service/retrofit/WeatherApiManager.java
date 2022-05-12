package starter.service.retrofit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;

@Service
public class WeatherApiManager {

    private static final String URL = "https://community-open-weather-map.p.rapidapi.com/";

    private final ClimateInterface climateInterface = createService(ClimateInterface.class);

    private static Retrofit.Builder builder = new Retrofit.Builder()
            .baseUrl(URL)
            .addConverterFactory(GsonConverterFactory.create());

    private static Retrofit retrofit = builder.build();

    private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

    public static <S> S createService(Class<S> serviceClass) {
        httpClient.interceptors().clear();

        httpClient.addInterceptor(chain -> {
            Request original = chain.request();
            Request request = original.newBuilder()
                    .header("x-rapidapi-host", "community-open-weather-map.p.rapidapi.com")
                    .header("x-rapidapi-key", "106a7d0624mshdb556a0a4461272p1d9408jsn33ead091c3af")
                    .build();
            return chain.proceed(request);
        });

        builder.client(httpClient.build());
        retrofit = builder.build();
        return retrofit.create(serviceClass);
    }

    @Cacheable("temperatures")
    public Response<WeatherInfo> getTemperature(String city) throws IOException {
        String metric = "metric";
        //It must always be metric system for the calculation to work as expected
        return climateInterface.getTemperature(city, metric).execute();
    }

}
