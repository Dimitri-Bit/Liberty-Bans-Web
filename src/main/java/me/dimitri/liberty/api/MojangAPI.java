package me.dimitri.liberty.api;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.micronaut.cache.annotation.Cacheable;
import jakarta.inject.Singleton;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

import java.io.IOException;

@Singleton
public class MojangAPI {

    private final OkHttpClient client;

    public MojangAPI() {
        client = new OkHttpClient();
    }

    @Cacheable("mojang-cache")
    public String usernameLookup(String uuid) {
        String json = getPlayerData(uuid);
        if (json != null) {
            JsonObject jsonObject = JsonParser.parseString(json).getAsJsonObject();
            return getUsername(jsonObject);
        }
        return "Unknown";
    }

    private String getPlayerData(String uuid) {
        try {
            String API_URL = "https://api.mojang.com/user/profile/";
            Request request = new Request.Builder()
                    .url(API_URL + uuid)
                    .build();

            try (Response response = client.newCall(request).execute()) {
                ResponseBody responseBody = response.body();
                if (response.isSuccessful() && responseBody != null) {
                    return responseBody.string();
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    private String getUsername(JsonObject jsonObject) {
        if (jsonObject.has("name")) {
            return jsonObject.get("name").getAsString();
        }
        return "Unknown";
    }
}