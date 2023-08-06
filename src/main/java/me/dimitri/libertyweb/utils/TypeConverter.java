package me.dimitri.libertyweb.utils;

import space.arim.libertybans.api.PunishmentType;

public class TypeConverter {
    public static PunishmentType getType(String type) {
        switch (type) {
            case "ban" -> {
                return PunishmentType.BAN;
            }
            case "mute" -> {
                return PunishmentType.MUTE;
            }
            case "warn" -> {
                return PunishmentType.WARN;
            }
            case "kick" -> {
                return PunishmentType.KICK;
            }
        }
        return PunishmentType.BAN;
    }
}
