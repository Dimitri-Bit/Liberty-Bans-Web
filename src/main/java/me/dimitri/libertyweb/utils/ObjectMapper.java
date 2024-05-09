package me.dimitri.libertyweb.utils;

import me.dimitri.libertyweb.api.LibertyWeb;
import me.dimitri.libertyweb.model.WebPunishment;
import space.arim.libertybans.api.PlayerOperator;
import space.arim.libertybans.api.PlayerVictim;
import space.arim.libertybans.api.punish.Punishment;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class ObjectMapper {

    private static final UUID CONSOLE_UUID = new UUID(0, 0);
    private static final SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

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

            webPunishment.setActive(getActiveSate(punishment));
            webPunishment.setLabel(getLabel(punishment));
            webPunishment.setReason(punishment.getReason());
            webPunishment.setPunishmentLength(getPunishmentLength(punishment));
            webPunishment.setStartDate(getDate(punishment.getStartDate(), punishment.getStartDateSeconds()));
            webPunishment.setEndDate(getDate(punishment.getEndDate(), punishment.getEndDateSeconds()));
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

    private static boolean getActiveSate(Punishment punishment) {
        return !punishment.isExpired();
    }

    private static String getPunishmentLength(Punishment punishment) {
        long punishmentStart = punishment.getStartDateSeconds();
        long punishmentEnd = punishment.getEndDateSeconds();

        if (punishmentStart > punishmentEnd) return "Forever";

        return differenceToTime(punishmentEnd - punishmentStart);
    }

    private static String differenceToTime(long difference) {
        if (difference < 60) {
            return difference + " seconds";
        }
        if (difference < 3600) {
            return (int)(difference / 60) + " minutes";
        }
        if (difference < 86400) {
            return (int)(difference / 3600) + " hours";
        }
        return (int)(difference / 86400) + " days";
    }

    private static String getDate(Instant instant, long seconds) {
        if (seconds == 0) return "Never";
        Date date = Date.from(instant);
        return formatter.format(date);
    }
}
