package me.dimitri.libertyweb.utils;

import jakarta.inject.Singleton;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Singleton
public class HttpRequestUtil {

    private final HttpClient client;

    public HttpRequestUtil() {
        client = HttpClient.newHttpClient();
    }

    public String get(String url) {
        try {
            URI uri = URI.create(url);
            HttpRequest request = HttpRequest.newBuilder().uri(uri).GET().build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                return response.body();
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}
