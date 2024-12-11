package cs1302.api;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * GoogleMapsAirQualityResponse class provides structure and the response from
 * the GoogleMapisAirQualityHeatMap api call.
 */
public class GoogleMapsAirQualityResponse extends BaseImageApiResponse {

    private String configPath = "resources/config.properties";
    // url needed to make the call
    private String url = "https://maps.googleapis.com/maps/api/airquality/v1/currentConditions";
    // apikey needed to make the call
    private String apiKey;


    /**
     * Attempts to retrieve Google Api Key.
     *
     * @throws RuntimeException if unable to get Key
     */
    public void getApiKey() {

        try (FileInputStream configFileStream = new FileInputStream(configPath)) {

            Properties config = new Properties();
            config.load(configFileStream);
            this.apiKey = config.getProperty("GoogleApi.key");


        } catch (IOException e) {

            throw new RuntimeException("Unexpected error when trying to retrieve Api Key"
                                       + " please try again");

        }
    }



    /**
     * Creates valid URI to make a valid call to the Google Maps Air Quality Heat
     * Map api.
     *
     * @param params the parameters needed to create a valid URI
     */
    public void createURI(String... params) {

        getApiKey();

        try {

            double lat = Double.parseDouble(params[0]);
            double lon = Double.parseDouble(params[1]);

            int[] coordinates = convertTileCordinates(lat, lon, 3);

            this.uri = String.format(
                "https://airquality.googleapis.com/v1/mapTypes/US_AQI/heatmapTiles/%d/%d/%d?key=%s",
                3, coordinates[0], coordinates[1], apiKey
            );

        } catch (Exception e) {
            throw new RuntimeException("Unexpected error occured when trying to create URI "
                                       + "make sure valid params.", e);
        }
    }

    /**
     * Turns latitude and longitutde values into Tile coordinates to be able
     * to be used by the Google maps Air Quality Heat Map api.
     *
     * @return int[] array table of the new Tile coordinates
     * @param latitude the lattitude passed through
     * @param longitude the longitude passed through
     * @param zoom zoom of the heat map
     */

    public static int[] convertTileCordinates(double latitude, double longitude, int zoom) {

        int x = (int) Math.floor((longitude + 180) / 360 * Math.pow(2, zoom));
        int y = (int) Math.floor(
            (1 - Math.log(Math.tan(Math.toRadians(latitude)) + 1 /
                          Math.cos(Math.toRadians(latitude))) / Math.PI) / 2
                          * Math.pow(2, zoom)
        );

        return new int[]{x, y};

    }
}
