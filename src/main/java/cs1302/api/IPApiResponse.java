package cs1302.api;

/**
 * IPAResponse class extends BaseJsonResponse and provides structure and response
 * from the GeoPlugin API call.
 */
public class IPApiResponse extends BaseJsonResponse {
    private String city;
    private String region;
    private String countryName;
    private String latitude;
    private String longitude;

    /**
     * Creates a valid URI for the GeoPlugin API call.
     *
     * @param params the parameters needed to create a valid URI
     */
    @Override
    public void createURI(String... params) {
        try {
            this.uri = "https://get.geojs.io/v1/ip/geo/" + params[0] + ".json";
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error occured when trying to create URI"
                                       + "make sure valid params.", e);
        }
    }

    /**
     * Returns the values of the response given by the API call.
     *
     * @return an array of variables returned by the API call
     */
    public Object[] returnResults() {
        return new Object[] {
            this.city,
            this.region,
            this.countryName,
            this.latitude,
            this.longitude
        };
    }

    /**
     * Uses the response from the API call and assigns the values to variables
     * using the wrapper class.
     *
     * @param response the response from the API call
     */
    @Override
    public void finializeResponse(String response) {
        LocationResponse locationResponse = gson.fromJson(response, LocationResponse.class);
        this.city = locationResponse.getCity();
        this.region = locationResponse.getRegion();
        this.countryName = locationResponse.getCountryName();
        this.latitude = locationResponse.getLatitude();
        this.longitude = locationResponse.getLongitude();
    }


    /**
     * Wrapper class for the GeoJS API call.
     */
    public static class LocationResponse {
        private String city;
        private String region;
        private String country;
        private String latitude;
        private String longitude;

        /**
         * Returns the results of the given city from the API call.
         *
         * @return city
         */
        public String getCity() {
            return city;
        }

        /**
         * Returns the results of the given region from the API call.
         *
         * @return region
         */
        public String getRegion() {
            return region;
        }

        /**
         * Returns the results of the given country name from the API call.
         *
         * @return country
         */
        public String getCountryName() {
            return country;
        }

        /**
         * Returns the results of the given latitude from the API call.
         *
         * @return latitude
         */
        public String getLatitude() {
            return latitude;
        }

        /**
         * Returns the results of the given longitude from the API call.
         *
         * @return longitude
         */
        public String getLongitude() {
            return longitude;
        }
    }
}
