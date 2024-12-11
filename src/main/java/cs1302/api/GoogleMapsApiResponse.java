package cs1302.api;

/**
 * GoogleMapsApiResponse class extends BaseImageApiResponse and
 * provieds structure and response from the GoogleMapsStaticMap Api call.
 */
public class GoogleMapsApiResponse extends BaseImageApiResponse {

    // url needed to make the call
    private String url = "https://maps.googleapis.com/maps/api/staticmap";
    // apikey needed to make the call
    private String apiKey = "AIzaSyCcD9l6CJk4o4RXdsASeRjhB1CJ4H6o9cw";

    /**
     * Creates valid URI to make a valid call to the Google Maps Static Api.
     *
     * @param params the paramaters needed to create a valid URI
     */
    @Override
    public void createURI(String... params) {

        try {

            String city = params[0];
            String country = params[1];
            String size = params[2];

            String focus = city.replace(" ", "+") + "," + country.replace(" ", "+");
            this.uri = String.format("%s?center=%s&zoom=%d&size=%s&key=%s",
                                     url, focus, 14, size, apiKey);
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error occured when trying to create URI"
                                       + "make sure valid params", e);
        }
    }
}
