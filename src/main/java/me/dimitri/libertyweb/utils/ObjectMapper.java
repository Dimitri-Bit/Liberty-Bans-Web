package me.dimitri.libertyweb.utils;

import me.dimitri.libertyweb.api.LibertyWeb;
import me.dimitri.libertyweb.model.WebPunishment;
import space.arim.libertybans.api.*;
import space.arim.libertybans.api.punish.Punishment;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.*;

public class ObjectMapper {

    private static final UUID CONSOLE_UUID = new UUID(0, 0);
    private static final SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

    public static void mapPunishments(List<WebPunishment> webPunishments, List<Punishment> punishments, LibertyWeb libertyWeb) {
        for (Punishment punishment : punishments) {
            WebPunishment webPunishment = new WebPunishment();

            if (punishment.getVictim().getType() == Victim.VictimType.COMPOSITE) {
                webPunishment.setVictimUuid(((CompositeVictim) punishment.getVictim()).getUUID().toString());
                webPunishment.setVictimAddress(obfuscatePlayerAddress(((CompositeVictim) punishment.getVictim()).getAddress()));
                webPunishment.setVictimUsername(lookupUsername(webPunishment.getVictimUuid(), libertyWeb));
            } else if (punishment.getVictim().getType() == Victim.VictimType.ADDRESS) {
                webPunishment.setVictimUuid("None");
                webPunishment.setVictimAddress(obfuscatePlayerAddress(((AddressVictim) punishment.getVictim()).getAddress()));
                webPunishment.setVictimUsername("Unknown");
            } else {
                // It would be possible to get the player's IP here, however since we are exposing punishments
                // we need to relay that this is only a UUID based punishment.
                webPunishment.setVictimUuid(((PlayerVictim) punishment.getVictim()).getUUID().toString());
                webPunishment.setVictimAddress("None");
                webPunishment.setVictimUsername(lookupUsername(webPunishment.getVictimUuid(), libertyWeb));
            }

            if (punishment.getOperator() instanceof PlayerOperator operator) {
                webPunishment.setOperatorUuid(operator.getUUID().toString());
                webPunishment.setOperatorUsername(lookupUsername(webPunishment.getOperatorUuid(), libertyWeb));
            } else {
                webPunishment.setOperatorUuid(CONSOLE_UUID.toString());
                webPunishment.setOperatorUsername("Console");
            }

            webPunishment.setLabel(getLabel(punishment));
            webPunishment.setType(getType(punishment));
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

    private static String getType(Punishment punishment) {
        if (punishment.getVictim().getType() == Victim.VictimType.COMPOSITE) {
            return "Composite";
        }
        if (punishment.getVictim().getType() == Victim.VictimType.ADDRESS) {
            return "Address";
        }
        if (punishment.getVictim().getType() == Victim.VictimType.PLAYER) {
            return "Player";
        }
        return "Unknown";
    }

    private static String obfuscatePlayerAddress(NetworkAddress networkAddress) {
        // In the actual API response we may not actually want to expose player IPs.
        // So let's hash it and append a fake TLD.
        // We may want to make this configurable in the future.
        String fakeTLD = ".liberty.web";
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException ignored) {
            return "Unsupported";
        }

        digest.digest(networkAddress.getRawAddress());

        String str = Base64.getEncoder().encodeToString(digest.digest(networkAddress.getRawAddress()));
        return str + fakeTLD;
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
