package me.dimitri.libertyweb.model.response;

import io.micronaut.serde.annotation.Serdeable;

@Serdeable.Serializable
public class StatisticsResponse {

    private int stats;

    public StatisticsResponse() {
        super();
    }

    public StatisticsResponse(int stats) {
        this. stats = stats;
    }

    public int getStats() {
        return stats;
    }
}
