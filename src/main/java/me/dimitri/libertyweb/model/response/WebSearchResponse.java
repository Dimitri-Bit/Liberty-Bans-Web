package me.dimitri.libertyweb.model.response;

import io.micronaut.serde.annotation.Serdeable;
import me.dimitri.libertyweb.model.WebPunishment;

import java.util.List;

@Serdeable.Serializable
public class WebSearchResponse {

    private String username;
    private String UUID;
    private List<WebPunishment> victimPunishments;
    private List<WebPunishment> operatorPunishments;

    public WebSearchResponse() {
        super();
    }

    public WebSearchResponse(String username, String UUID, List<WebPunishment> victimPunishments, List<WebPunishment> operatorPunishments) {
        this.username = username;
        this.UUID = UUID;
        this.victimPunishments = victimPunishments;
        this.operatorPunishments = operatorPunishments;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUUID() {
        return UUID;
    }

    public void setUUID(String UUID) {
        this.UUID = UUID;
    }

    public List<WebPunishment> getVictimPunishments() {
        return victimPunishments;
    }

    public void setVictimPunishments(List<WebPunishment> victimPunishments) {
        this.victimPunishments = victimPunishments;
    }

    public List<WebPunishment> getOperatorPunishments() {
        return operatorPunishments;
    }

    public void setOperatorPunishments(List<WebPunishment> operatorPunishments) {
        this.operatorPunishments = operatorPunishments;
    }
}
