package me.dimitri.libertyweb.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.micronaut.cache.annotation.Cacheable;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import me.dimitri.libertyweb.utils.HttpRequestUtil;

import java.io.IOException;

@Singleton
public class UsernameAPI {

    private final HttpRequestUtil requestUtil;
    private final ObjectMapper objectMapper;
    private final String MOJANG_URL = "https://api.mojang.com/user/profile/";
    private final String CRAFTHEAD_URL = "https://crafthead.net/profile/";

    @Inject
    public UsernameAPI(HttpRequestUtil requestUtil) {
        this.requestUtil = requestUtil;
        objectMapper = new ObjectMapper();
    }

    @Cacheable("mojang-cache")
    public String usernameLookup(String uuid) {
        String json = requestUtil.get(MOJANG_URL + uuid);

        if (json == null) {
            json = requestUtil.get(CRAFTHEAD_URL + uuid);
        }
        if (json != null) {
            JsonNode jsonNode = parseJson(json);
            return getUsername(jsonNode);
        }
        return "Unknown";
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