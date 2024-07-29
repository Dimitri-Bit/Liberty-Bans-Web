package me.dimitri.libertyweb.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.micronaut.cache.annotation.Cacheable;
import io.micronaut.context.annotation.Primary;
import jakarta.inject.Singleton;
import me.dimitri.libertyweb.service.UsernameService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Primary
@Singleton
public class UsernameServiceImpl implements UsernameService {

    private static final String MOJANG_URL = "https://api.mojang.com/user/profile/";
    private static final String CRAFTHEAD_URL = "https://crafthead.net/profile/";
    private static final Logger logger = LoggerFactory.getLogger(UsernameServiceImpl.class);

    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    @Cacheable("username-cache")
    public String lookupUsernameFromUUID(String uuid) {
        String mojangResponse = getRequest(MOJANG_URL, uuid);
        if (mojangResponse != null) {
            return deserializeUsername(mojangResponse);
        }

        String craftHeadResponse = getRequest(CRAFTHEAD_URL, uuid);
        if (craftHeadResponse != null) {
            return deserializeUsername(craftHeadResponse);
        }

        return "Unknown";
    }

    private String deserializeUsername(String json) {
        try {
            JsonNode jsonNode = objectMapper.readTree(json);
            if (jsonNode.has("name")) {
                return jsonNode.get("name").asText();
            }
        } catch (JsonProcessingException ignored) {
            // ignored
        }
        return "Unknown";
    }

    private String getRequest(String url, String uuid) {
        try {
            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(URI.create(url + uuid))
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200)
                return null;

            return response.body();
        } catch (IOException | InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.warn(String.format("Unable to reach %s", url));
        }
        return null;
    }

}
