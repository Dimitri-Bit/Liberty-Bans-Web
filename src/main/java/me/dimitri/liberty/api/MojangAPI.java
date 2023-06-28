package me.dimitri.liberty.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.micronaut.cache.annotation.Cacheable;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import me.dimitri.liberty.utils.HttpRequestUtil;

import java.io.IOException;

@Singleton
public class MojangAPI {

    private final HttpRequestUtil requestUtil;
    private final ObjectMapper objectMapper;

    @Inject
    public MojangAPI(HttpRequestUtil requestUtil) {
        this.requestUtil = requestUtil;
        objectMapper = new ObjectMapper();
    }

    @Cacheable("mojang-cache")
    public String usernameLookup(String uuid) {
        String json = requestUtil.get("https://api.mojang.com/user/profile/" + uuid);
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