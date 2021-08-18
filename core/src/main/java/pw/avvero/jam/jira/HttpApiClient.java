package pw.avvero.jam.jira;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import pw.avvero.jam.JamException;
import pw.avvero.jam.jira.dto.ErrorResponse;

import java.io.IOException;
import java.net.ProxySelector;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Base64;

import static pw.avvero.jam.core.SerializationUtils.read;
import static pw.avvero.jam.core.SerializationUtils.stringify;

@Slf4j
@AllArgsConstructor
public class HttpApiClient {

    private final String host;
    private final String username;
    private final String password;
    private final int connectTimeout;

    public <T> T requestGet(String method, Class<T> clazz) {
        String uri = host + method;
        log.debug("Calling GET: " + uri);
        try {
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
                    .connectTimeout(Duration.ofMillis(connectTimeout))
                    .followRedirects(HttpClient.Redirect.ALWAYS)
                    .build()
                    .send(request, HttpResponse.BodyHandlers.ofString());
            String responseBody = response.body();
            log.debug("Response payload: {}: {}", response.statusCode(), responseBody);
            if (response.statusCode() == 404) {
                return null;
            } else if (response.statusCode() >= 400) {
                throw errorFrom(response);
            } else {
                return read(responseBody, clazz);
            }
        } catch (InterruptedException | URISyntaxException | IOException | JamException e) {
            throw new RuntimeException(String.format("Error with request to %s: %s", uri, e.getLocalizedMessage()), e);
        }
    }

    public <T> T requestPost(String method, Object payload, Class<T> clazz) throws JamException {
        String uri = host + method;
        log.debug("Calling POST: " + uri);
        try {
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
                    .connectTimeout(Duration.ofMillis(connectTimeout))
                    .followRedirects(HttpClient.Redirect.ALWAYS)
                    .build()
                    .send(request, HttpResponse.BodyHandlers.ofString());
            String responseBody = response.body();
            log.debug("Response payload: {}: {}", response.statusCode(), responseBody);
            if (response.statusCode() >= 400) {
                throw errorFrom(response);
            } else {
                return read(responseBody, clazz);
            }
        } catch (InterruptedException | URISyntaxException | IOException | JamException e) {
            throw new RuntimeException(String.format("Error with request to %s: %s", uri, e.getLocalizedMessage()), e);
        }
    }

    public <T> T requestPut(String method, Object payload, Class<T> clazz) {
        String uri = host + method;
        log.debug("Calling PUT: " + uri);
        try {
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
                    .connectTimeout(Duration.ofMillis(connectTimeout))
                    .followRedirects(HttpClient.Redirect.ALWAYS)
                    .build()
                    .send(request, HttpResponse.BodyHandlers.ofString());
            String responseBody = response.body();
            log.debug("Response payload: {}: {}", response.statusCode(), responseBody);
            if (response.statusCode() == 404) {
                return null;
            } else if (response.statusCode() >= 400) {
                throw errorFrom(response);
            } else {
                return read(responseBody, clazz);
            }
        } catch (InterruptedException | URISyntaxException | IOException | JamException e) {
            throw new RuntimeException(String.format("Error with request to %s: %s", uri, e.getLocalizedMessage()), e);
        }
    }

    public void requestDelete(String method) {
        try {
            String uri = host + method;
            log.debug("Calling: " + uri);
            String auth = username + ":" + password;
            byte[] encodedAuth = Base64.getEncoder().encode(auth.getBytes(StandardCharsets.US_ASCII));
            String authHeader = "Basic " + new String(encodedAuth);
            HttpRequest request = HttpRequest.newBuilder()
                    .DELETE()
                    .uri(new URI(uri))
                    .headers("Authorization", authHeader)
                    .build();
            HttpResponse<String> response = HttpClient
                    .newBuilder()
                    .proxy(ProxySelector.getDefault())
                    .build()
                    .send(request, HttpResponse.BodyHandlers.ofString());
            String responseBody = response.body();
            log.debug("Response payload: {}: {}", response.statusCode(), responseBody);
            if (response.statusCode() >= 400) {
                throw errorFrom(response);
            }
        } catch (InterruptedException | URISyntaxException | IOException | JamException e) {
            throw new RuntimeException(e.getLocalizedMessage(), e);
        }
    }

    private JamException errorFrom(HttpResponse<String> response) throws IOException, JamException {
        String responseBody = response.body();
        ErrorResponse errorResponse = read(responseBody, ErrorResponse.class);
        return new JamException(errorResponse.getErrors().values().stream().findFirst().orElse("Unknown error"));
    }
}
