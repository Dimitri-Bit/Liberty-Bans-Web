package me.dimitri.libertyweb.service;

import jakarta.inject.Singleton;
import me.dimitri.libertyweb.api.MojangAPI;
import me.dimitri.libertyweb.model.WebPunishment;
import me.dimitri.libertyweb.model.WebPunishmentResponse;
import me.dimitri.libertyweb.repository.PunishmentsRepository;
import space.arim.libertybans.api.PunishmentType;

import java.util.List;

@Singleton
public class PunishmentService {
    private final PunishmentsRepository punishmentsRepository;
    private final MojangAPI mojangAPI;
    private final int MAX_ROWS = 6;

    public PunishmentService(PunishmentsRepository punishmentsRepository, MojangAPI mojangAPI) {
        this.punishmentsRepository = punishmentsRepository;
        this.mojangAPI = mojangAPI;
    }

    public WebPunishmentResponse getPunishments(String type, String page) {
        int pageNum;
        try { pageNum = Integer.parseInt(page); } catch (NumberFormatException ignored) { return null; }

        PunishmentType punishmentType = getType(type);
        WebPunishmentResponse response = punishmentsRepository.query(punishmentType, calculateOffset(pageNum));
        fixUsernames(response.getPunishments());
        return response;
    }

    private int calculateOffset(int page) {
        return (page - 1) * MAX_ROWS;
    }

    private void fixUsernames(List<WebPunishment> punishments) {
        for (WebPunishment punishment : punishments) {
            if (punishment.getVictimUsername().equals("Unknown")) {
                String username = mojangAPI.usernameLookup(punishment.getVictimUuid());
                punishment.setVictimUsername(username);
            }
        }
    }

    private PunishmentType getType(String type) {
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
