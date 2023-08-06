package me.dimitri.libertyweb.model.response;

import io.micronaut.serde.annotation.Serdeable;
import me.dimitri.libertyweb.model.WebPunishment;

import java.util.ArrayList;
import java.util.List;

@Serdeable.Serializable
public class WebPunishmentResponse {
    private boolean morePages;
    private List<WebPunishment> punishments = new ArrayList<>();

    public WebPunishmentResponse() {
        super();
    }

    public WebPunishmentResponse(boolean morePages, List<WebPunishment> punishments) {
        this.morePages = morePages;
        this.punishments = punishments;
    }

    public boolean isMorePages() {
        return morePages;
    }

    public void setMorePages(boolean morePages) {
        this.morePages = morePages;
    }

    public List<WebPunishment> getPunishments() {
        return punishments;
    }

    public void setPunishments(List<WebPunishment> punishments) {
        this.punishments = punishments;
    }
}
