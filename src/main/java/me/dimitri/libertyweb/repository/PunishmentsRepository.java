package me.dimitri.libertyweb.repository;

import jakarta.inject.Singleton;
import me.dimitri.libertyweb.api.LibertyWeb;
import me.dimitri.libertyweb.model.WebPunishment;
import me.dimitri.libertyweb.model.WebPunishmentResponse;
import space.arim.libertybans.api.Operator;
import space.arim.libertybans.api.PunishmentType;
import space.arim.libertybans.api.Victim;
import space.arim.libertybans.api.punish.Punishment;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Singleton
public class PunishmentsRepository {

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

            webPunishment.setVictimUuid(extractUUID(punishment.getVictim().toString()));
            webPunishment.setVictimUsername(lookupUsername(webPunishment.getVictimUuid()));
            if (punishment.getOperator().getType().equals(Operator.OperatorType.CONSOLE)) {
                webPunishment.setOperatorUuid("f78a4d8dd51b4b3998a3230f2de0c670");
                webPunishment.setOperatorUsername("Console");
            } else {
                webPunishment.setOperatorUuid(extractUUID(punishment.getOperator().toString()));
                webPunishment.setOperatorUuid(lookupUsername(webPunishment.getOperatorUuid()));
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
