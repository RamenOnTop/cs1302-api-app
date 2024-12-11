package cs1302.api;

/**
 * WeatherVisualResponse class extends BaseImageApiResponse and
 * provides structure and a response from the 7times API call.
 */
public class WeatherVisualResponse extends BaseImageApiResponse {

    /**
     * Creates a valid URI to make a valid call for a Weather Visual.
     *
     * @params params the parameters needed to create a valid URI
     */
    @Override
    public void createURI(String... params) {

        try {

            String latitude = params[0];
            String longitude = params[1];

            this.uri = String.format("https://www.7timer.info/bin/astro.php?lon=%s&lat=%s&"
                                     + "output=png",
                                     longitude, latitude);
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error occured when trying to create URI"
                                       + "make sure valid params.", e);
        }
    }
}
