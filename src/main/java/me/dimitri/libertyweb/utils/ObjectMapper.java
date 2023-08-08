package me.dimitri.libertyweb.utils;

import me.dimitri.libertyweb.api.LibertyWeb;
import me.dimitri.libertyweb.model.WebPunishment;
import space.arim.libertybans.api.PlayerOperator;
import space.arim.libertybans.api.PlayerVictim;
import space.arim.libertybans.api.punish.Punishment;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class ObjectMapper {

    private static final UUID CONSOLE_UUID = new UUID(0, 0);

    public static void mapPunishments(List<WebPunishment> webPunishments, List<Punishment> punishments, LibertyWeb libertyWeb) {
        for (Punishment punishment : punishments) {
            WebPunishment webPunishment = new WebPunishment();

            webPunishment.setVictimUuid(((PlayerVictim) punishment.getVictim()).getUUID().toString());
            webPunishment.setVictimUsername(lookupUsername(webPunishment.getVictimUuid(), libertyWeb));
            if (punishment.getOperator() instanceof PlayerOperator operator) {
                webPunishment.setOperatorUuid(operator.getUUID().toString());
                webPunishment.setOperatorUsername(lookupUsername(webPunishment.getOperatorUuid(), libertyWeb));
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

    private static String lookupUsername(String UUID, LibertyWeb libertyWeb) {
        Optional<String> username = libertyWeb.getApi().getUserResolver().lookupName(java.util.UUID.fromString(UUID)).join();
        return username.orElse("Unknown");
    }

    private static String getLabel(Punishment punishment) {
        if (punishment.isExpired()) {
            return "Expired";
        }
        if (punishment.isTemporary()) {
            return "Active";
        }
        if (punishment.isPermanent()) {
            return "Permanent";
        }
        return "Unknown";
    }
}
