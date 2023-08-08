package me.dimitri.libertyweb.repository;

import jakarta.inject.Singleton;
import me.dimitri.libertyweb.api.LibertyWeb;
import me.dimitri.libertyweb.model.WebPunishment;
import me.dimitri.libertyweb.model.response.WebPunishmentResponse;
import space.arim.libertybans.api.*;
import space.arim.libertybans.api.punish.Punishment;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static me.dimitri.libertyweb.utils.ObjectMapper.mapPunishments;

@Singleton
public class PunishmentsRepository {

    private static final UUID CONSOLE_UUID = new UUID(0, 0);

    private final LibertyWeb libertyWeb;

    public PunishmentsRepository(LibertyWeb libertyWeb) {
        this.libertyWeb = libertyWeb;
    }

    public WebPunishmentResponse query(PunishmentType punishmentType, int offset) {
        List<WebPunishment> webPunishments = new ArrayList<>();

        List<Punishment> punishments = libertyWeb.getApi().getSelector()
                .selectionBuilder()
                .selectAll()
                .type(punishmentType)
                .victimType(Victim.VictimType.PLAYER)
                .skipFirstRetrieved(offset)
                .limitToRetrieve(6)
                .build()
                .getAllSpecificPunishments()
                .toCompletableFuture()
                .join();

        mapPunishments(webPunishments, punishments, libertyWeb);
        return new WebPunishmentResponse(webPunishments.size() == 6, webPunishments);
    }
}
