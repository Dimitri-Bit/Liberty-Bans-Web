package me.dimitri.libertyweb.repository;

import jakarta.inject.Singleton;
import me.dimitri.libertyweb.api.LibertyWeb;
import me.dimitri.libertyweb.model.WebPunishment;
import me.dimitri.libertyweb.model.WebPunishmentResponse;
import space.arim.libertybans.api.*;
import space.arim.libertybans.api.punish.Punishment;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

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

        mapPunishments(webPunishments, punishments);
        return new WebPunishmentResponse(webPunishments.size() == 6, webPunishments);
    }

    private void mapPunishments(List<WebPunishment> webPunishments, List<Punishment> punishments) {
        for (Punishment punishment : punishments) {
            WebPunishment webPunishment = new WebPunishment();

            webPunishment.setVictimUuid(((PlayerVictim) punishment.getVictim()).getUUID().toString());
            webPunishment.setVictimUsername(lookupUsername(webPunishment.getVictimUuid()));
            if (punishment.getOperator() instanceof PlayerOperator operator) {
                webPunishment.setOperatorUuid(operator.getUUID().toString());
                webPunishment.setOperatorUsername(lookupUsername(webPunishment.getOperatorUuid()));
            } else {
                webPunishment.setOperatorUuid(CONSOLE_UUID.toString());
                webPunishment.setOperatorUsername("Console");
            }

            webPunishment.setLabel(getLabel(punishment));
            webPunishment.setStart(punishment.getStartDateSeconds());
            webPunishment.setEnd(punishment.getEndDateSeconds());
            webPunishment.setReason(punishment.getReason());
            webPunishments.add(webPunishment);
        }
    }

    private String extractUUID(String string) {
        string = string.split("=")[1];
        string = string.replaceFirst(".$","");
        return string;
    }

    private String lookupUsername(String UUID) {
        Optional<String> username = libertyWeb.getApi().getUserResolver().lookupName(java.util.UUID.fromString(UUID)).join();
        return username.orElse("Unknown");
    }

    private String getLabel(Punishment punishment) {
        if (punishment.isExpired()) {
            return "Expired";
        }
        if (punishment.isTemporary() && !punishment.isExpired()) {
            return "Active";
        }
        if (punishment.isPermanent() && !punishment.isExpired()) {
            return "Permanent";
        }
        return "Unknown";
    }
}
