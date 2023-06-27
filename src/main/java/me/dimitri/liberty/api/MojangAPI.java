package me.dimitri.liberty.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.micronaut.cache.annotation.Cacheable;
import jakarta.inject.Singleton;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Singleton
public class MojangAPI {

    private final HttpClient client;
    private final ObjectMapper objectMapper;

    public MojangAPI() {
        client = HttpClient.newHttpClient();
        objectMapper = new ObjectMapper();
    }

    @Cacheable("mojang-cache")
    public String usernameLookup(String uuid) {
        String json = getPlayerData(uuid);
        if (json != null) {
            JsonNode jsonNode = parseJson(json);
            return getUsername(jsonNode);
        }
        return "Unknown";
    }

    private String getPlayerData(String uuid) {
        try {
            String API_URL = "https://api.mojang.com/user/profile/";
            URI uri = URI.create(API_URL + uuid);
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

    private JsonNode parseJson(String json) {
        try {
            return objectMapper.readTree(json);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String getUsername(JsonNode jsonNode) {
        if (jsonNode.has("name")) {
            return jsonNode.get("name").asText();
        }
        return "Unknown";
    }
}