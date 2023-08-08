package me.dimitri.libertyweb.repository;

import jakarta.inject.Singleton;
import me.dimitri.libertyweb.api.LibertyWeb;
import me.dimitri.libertyweb.model.WebPunishment;
import me.dimitri.libertyweb.model.response.WebSearchResponse;
import space.arim.libertybans.api.PlayerOperator;
import space.arim.libertybans.api.PlayerVictim;
import space.arim.libertybans.api.punish.Punishment;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static me.dimitri.libertyweb.utils.ObjectMapper.mapPunishments;

@Singleton
public class SearchRepository {

    private final LibertyWeb libertyWeb;

    public SearchRepository(LibertyWeb libertyWeb) {
        this.libertyWeb = libertyWeb;
    }

    public WebSearchResponse query(UUID UUID, String username) {
        List<WebPunishment> victimWebPunishments = new ArrayList<>();
        List<WebPunishment> operatorWebPunishments = new ArrayList<>();

        List<Punishment> victimPunishments = libertyWeb.getApi().getSelector()
                .selectionBuilder()
                .selectAll()
                .victim(PlayerVictim.of(UUID))
                .build()
                .getAllSpecificPunishments()
                .toCompletableFuture()
                .join();

        List<Punishment> operatorPunishments = libertyWeb.getApi().getSelector()
                .selectionBuilder()
                .selectAll()
                .operator(PlayerOperator.of(UUID))
                .build()
                .getAllSpecificPunishments()
                .toCompletableFuture()
                .join();

        mapPunishments(victimWebPunishments, victimPunishments, libertyWeb);
        mapPunishments(operatorWebPunishments, operatorPunishments, libertyWeb);
        return new WebSearchResponse(username, UUID.toString(), victimWebPunishments, operatorWebPunishments);
    }

}
