package me.dimitri.libertyweb.repository;

import jakarta.inject.Singleton;
import me.dimitri.libertyweb.api.LibertyWeb;
import me.dimitri.libertyweb.model.response.StatisticsResponse;
import space.arim.libertybans.api.PunishmentType;

@Singleton
public class StatisticsRepository {

    private final LibertyWeb libertyWeb;

    public StatisticsRepository(LibertyWeb libertyWeb) {
        this.libertyWeb = libertyWeb;
    }

    public StatisticsResponse query(PunishmentType punishmentType) {
        int stats = libertyWeb.getApi().getSelector()
                .selectionBuilder()
                .selectAll()
                .type(punishmentType)
                .build()
                .countNumberOfPunishments()
                .toCompletableFuture()
                .join();
        return new StatisticsResponse(stats);
    }
}
