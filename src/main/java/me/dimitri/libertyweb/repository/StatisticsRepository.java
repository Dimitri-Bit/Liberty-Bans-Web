package me.dimitri.libertyweb.repository;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import me.dimitri.libertyweb.api.LibertyWeb;
import me.dimitri.libertyweb.model.response.StatisticsResponse;
import space.arim.libertybans.api.PunishmentType;
import space.arim.libertybans.api.select.SelectionOrderBuilder;

import java.util.Optional;

@Singleton
public class StatisticsRepository {

    private final LibertyWeb libertyWeb;

    @Inject
    public StatisticsRepository(LibertyWeb libertyWeb) {
        this.libertyWeb = libertyWeb;
    }

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    public StatisticsResponse query(Optional<PunishmentType> punishmentType) {
        SelectionOrderBuilder selectionOrderBuilder = libertyWeb.getApi().getSelector().selectionBuilder();

        selectionOrderBuilder.selectAll();
        punishmentType.ifPresent(selectionOrderBuilder::type);

        int stats = selectionOrderBuilder.build().countNumberOfPunishments().toCompletableFuture().join();

        return new StatisticsResponse(stats);
    }
}
