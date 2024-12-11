package cs1302.api;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import com.google.gson.Gson;

/**
 * BaseJsonResponse class provides a base structure for Api Responses
 * that use json as their response type.
 */
public abstract class BaseJsonResponse {

    // cotructing uri for futher use
    protected String uri;
    // HTTP client used to send api request
    protected HttpClient httpClient = HttpClient.newBuilder().build();
    // gson used to understand api resposne
    protected Gson gson = new Gson();

    /**
     * Creates the URI to be used for the API request.
     *
     * @param params the paramaters needed to create valid URI
     */
    public abstract void createURI(String... params);

    /**
     * Method that parses the jsonReponse data and assings them to
     * variables created.
     *
     * @param jsonResponse reponse from the api call
     */
    protected abstract void finializeResponse(String jsonResponse);

    /**
     * Uses {@code uri} to make a valid api call and and create a valid request
     * and response.
     */
    public void createResults() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(this.uri))
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.
                                                            BodyHandlers.ofString());
            finializeResponse(response.body());
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error. . . Invalid IP?", e);
        }
    }
}
