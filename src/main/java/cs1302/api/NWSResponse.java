package cs1302.api;

import java.util.*;

import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import com.google.gson.Gson;

/**
 * NWSReponse class extends BaseJsonResponse class and provides
 * structure and response from the api.weather.gov API call.
 */
public class NWSResponse extends BaseJsonResponse {

    private String url = "https://api.weather.gov/points/";
    private String weatherDescription;

    /**
     * Creates valid URI to make a valid call to the api weather API.
     *
     * @params params the paramaters needed to create a valid URI
     */
    @Override
    public void createURI(String... params) {
        try {
            this.uri = String.format("%s%s,%s", url, params[0], params[1]);
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error occured when trying to create URI"
                                       + "make sure valid params.", e);
        }
    }

    /**
     * Returns the value {@code weatherDescription}.
     *
     * @return the weatherDescription of the area current time
     */
    public String returnResults() {
        return this.weatherDescription;
    }

    /**
     * Uses the response from the API call and assigns the values to variables using wrapper class.
     *
     * @param response the response from the API call
     */
    @Override
    public void finializeResponse(String response) {
        ForecastsResponse fr = gson.fromJson(response, ForecastsResponse.class);
        if (fr.properties.periods != null && !fr.properties.periods.isEmpty()) {
            ForecastsResponse.Period firstPeriod = fr.properties.periods.get(0);
            this.weatherDescription = firstPeriod.name + ": " + firstPeriod.detailedForecast;
        }
    }

    /**
     * Uses {@code uri} to make multiple valid api calls and creates valid multiple request.
     */
    @Override
    public void createResults() {
        try {
            HttpRequest pointsRequest = HttpRequest.newBuilder()
                .uri(URI.create(this.uri))
                .header("User-Agent", "Java Weather App")
                .build();

            HttpResponse<String> pointsResponse = httpClient.send(pointsRequest,
                                                                  HttpResponse.
                                                                  BodyHandlers.ofString());
            PointsResponse pointsData = gson.fromJson(pointsResponse.body(), PointsResponse.class);
            String forecastURL = pointsData.properties.forecast;

            HttpRequest forecastRequest = HttpRequest.newBuilder()
                .uri(URI.create(forecastURL))
                .header("User-Agent", "Java Weather App")
                .build();

            HttpResponse<String> forecastResponse = httpClient.send(forecastRequest,
                                                                    HttpResponse.
                                                                    BodyHandlers.ofString());
            finializeResponse(forecastResponse.body());

        } catch (Exception e) {
            throw new RuntimeException("Unexpected error occured. . . ", e);
        }
    }

    /**
     * Wrapper class for the api weather gov API.
     */
    public static class PointsResponse {
        public Properties properties;

        /**
         * Represents the prorties section of the points response.
         */
        public static class Properties {
            public String forecast;
        }
    }

    /**
     * Wrapper class for the api weather gov API.
     */
    public static class ForecastsResponse {
        public Properties properties;

        /**
         * Represents the properties section of the Forecasts response.
         */
        public static class Properties {
            public List<Period> periods;
        }

        /**
         * Represents an individual forecast period.
         */
        public static class Period {
            public String name;
            public String detailedForecast;
        }
    }
}
