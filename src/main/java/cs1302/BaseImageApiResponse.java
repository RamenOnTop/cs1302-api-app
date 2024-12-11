package cs1302.api;

import java.util.*;

import java.net.URI;
import java.net.http.*;

import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * BaseImageApiResponse class provides a base structure for Api
 * Responses that included image retrivial as their response to being
 * called.
 */
public abstract class BaseImageApiResponse {

    // contructing uri for further use.
    protected String uri;

    // HTTP client used to send api request.
    protected HttpClient httpClient = HttpClient.newBuilder().build();

    /**
     * Creates the URI to be used for the API request.
     *
     * @param params the paramaters needed to create valid URI
     */
    public abstract void createURI(String... params);

    /**
     * gets the requests results from the {@code uri}.
     *
     * @param outputFilePath the file path where the response should be saved
     */
    public void createResults(String outputFilePath) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(uri))
                    .GET()
                    .build();

            HttpResponse<byte[]> response = httpClient.send(request, HttpResponse.
                                                            BodyHandlers.ofByteArray());
            Files.write(Paths.get(outputFilePath), response.body());
        } catch (Exception e) {
            throw new RuntimeException("Unexpected eroor. . .", e);
        }
    }
}
