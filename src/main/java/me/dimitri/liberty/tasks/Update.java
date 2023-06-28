package me.dimitri.liberty.tasks;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.micronaut.context.annotation.Value;
import io.micronaut.scheduling.annotation.Scheduled;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import me.dimitri.liberty.model.Release;
import me.dimitri.liberty.utils.HttpRequestUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

@Singleton
public class Update implements Runnable {

    @Value("${version}")
    String version;
    private final Logger log = LoggerFactory.getLogger(Update.class);
    private final HttpRequestUtil requestUtil;
    private final ObjectMapper objectMapper;

    private boolean running = true;

    @Inject
    public Update(HttpRequestUtil requestUtil) {
        this.requestUtil = requestUtil;
        objectMapper = new ObjectMapper();
    }

    @Scheduled(fixedDelay = "60m")
    @Override
    public void run() {
        if (!running) {
            return;
        }

        Release release = getLatestRelease();
        if (release != null) {
            if (!release.getTag_name().equals(version)) {
                printMessage(release);
                running = false;
            }
        }
    }

    private void printMessage(Release release) {
        log.warn("New Version Detected: " + release.getName() + "(" + release.getTag_name() + ")");
        log.warn("Download the latest version to be up-to date with the latest features & bug fixes.");
        log.warn("https://github.com/Dimitri-Bit/Liberty-Bans-Web");
    }

    private Release getLatestRelease() {
        try {
            String json = requestUtil.get("https://api.github.com/repositories/645537003/releases");
            if (json != null) {
                JsonNode rootNode = parseJson(json);
                JsonNode releaseNode = rootNode.get(0);
                return objectMapper.treeToValue(releaseNode, Release.class);
            }
        } catch (JsonProcessingException e) {
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
}
