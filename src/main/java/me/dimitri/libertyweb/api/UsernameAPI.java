package me.dimitri.libertyweb.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.micronaut.cache.annotation.Cacheable;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import me.dimitri.libertyweb.Application;
import me.dimitri.libertyweb.utils.HttpRequestUtil;
import me.dimitri.libertyweb.utils.exception.HttpErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

@Singleton
public class UsernameAPI {

    private static final Logger log = LoggerFactory.getLogger(UsernameAPI.class);
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
        String json = null;
        try {
            json = requestUtil.get(MOJANG_URL + uuid);
        } catch (HttpErrorException e) {
            log.error(e.getMessage());
        }

        if (json == null) {
            try {
                json = requestUtil.get(CRAFTHEAD_URL + uuid);
            } catch (HttpErrorException e) {
                log.error(e.getMessage());
            }
        }

        if (json != null) {
            JsonNode jsonNode = parseJson(json);
            return getUsername(jsonNode);
        }
        return "Unknown";
    }

    private JsonNode parseJson(String json){
        try {
            return objectMapper.readTree(json);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        return null;
    }

    private String getUsername(JsonNode jsonNode) {
        if (jsonNode.has("name")) {
            return jsonNode.get("name").asText();
        }
        return "Unknown";
    }
}