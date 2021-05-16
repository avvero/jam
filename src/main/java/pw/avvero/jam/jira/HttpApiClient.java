package pw.avvero.jam.jira;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.ProxySelector;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import static pw.avvero.jam.core.SerializationUtils.read;
import static pw.avvero.jam.core.SerializationUtils.stringify;

@Slf4j
@AllArgsConstructor
public class HttpApiClient {

    private final String host;
    private final String username;
    private final String password;

    public <T> T requestGet(String method, Class<T> clazz) {
        try {
            String uri = host + method;
            log.debug("Calling: " + uri);
            String auth = username + ":" + password;
            byte[] encodedAuth = Base64.getEncoder().encode(auth.getBytes(StandardCharsets.US_ASCII));
            String authHeader = "Basic " + new String(encodedAuth);

            HttpRequest request = HttpRequest.newBuilder()
                    .GET()
                    .uri(new URI(uri))
                    .headers("Authorization", authHeader)
                    .build();
            HttpResponse<String> response = HttpClient
                    .newBuilder()
                    .proxy(ProxySelector.getDefault())
                    .build()
                    .send(request, HttpResponse.BodyHandlers.ofString());
            log.debug("Response payload: {}: {}", response.statusCode(), response.body());
            if (response.statusCode() == 404) {
                return null;
            }
            return read(response.body(), clazz);
        } catch (InterruptedException | URISyntaxException | IOException e) {
            throw new RuntimeException(e.getLocalizedMessage(), e);
        }
    }

    public <T> T requestPost(String method, Object payload, Class<T> clazz) {
        try {
            String uri = host + method;
            log.debug("Calling: " + uri);
            String auth = username + ":" + password;
            byte[] encodedAuth = Base64.getEncoder().encode(auth.getBytes(StandardCharsets.US_ASCII));
            String authHeader = "Basic " + new String(encodedAuth);

            String payloadString = stringify(payload);
            log.debug("Payload: " + payloadString);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(uri))
                    .headers("Authorization", authHeader)
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(payloadString))
                    .build();
            HttpResponse<String> response = HttpClient
                    .newBuilder()
                    .proxy(ProxySelector.getDefault())
                    .build()
                    .send(request, HttpResponse.BodyHandlers.ofString());
            log.debug("Response code: " + response.statusCode());
            String responseBody = response.body();
            log.debug("Response payload: {}: {}", response.statusCode(), response.body());
            return read(responseBody, clazz);
        } catch (InterruptedException | URISyntaxException | IOException e) {
            throw new RuntimeException(e.getLocalizedMessage(), e);
        }
    }

    public <T> T requestPut(String method, Object payload, Class<T> clazz) {
        try {
            String uri = host + method;
            log.debug("Calling: " + uri);
            String auth = username + ":" + password;
            byte[] encodedAuth = Base64.getEncoder().encode(auth.getBytes(StandardCharsets.US_ASCII));
            String authHeader = "Basic " + new String(encodedAuth);

            String payloadString = stringify(payload);
            log.debug("Payload: " + payloadString);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(uri))
                    .headers("Authorization", authHeader)
                    .header("Content-Type", "application/json")
                    .PUT(HttpRequest.BodyPublishers.ofString(payloadString))
                    .build();
            HttpResponse<String> response = HttpClient
                    .newBuilder()
                    .proxy(ProxySelector.getDefault())
                    .build()
                    .send(request, HttpResponse.BodyHandlers.ofString());
            log.debug("Response code: " + response.statusCode());
            String responseBody = response.body();
            log.debug("Response payload: {}: {}", response.statusCode(), response.body());
            return read(responseBody, clazz);
        } catch (InterruptedException | URISyntaxException | IOException e) {
            throw new RuntimeException(e.getLocalizedMessage(), e);
        }
    }

}
