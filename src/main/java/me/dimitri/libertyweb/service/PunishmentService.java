package me.dimitri.libertyweb.service;

import jakarta.inject.Singleton;
import me.dimitri.libertyweb.api.UsernameAPI;
import me.dimitri.libertyweb.model.WebPunishment;
import me.dimitri.libertyweb.model.response.WebPunishmentResponse;
import me.dimitri.libertyweb.repository.PunishmentsRepository;
import space.arim.libertybans.api.PunishmentType;

import java.util.List;

import static me.dimitri.libertyweb.utils.TypeConverter.getType;

@Singleton
public class PunishmentService {
    private final PunishmentsRepository punishmentsRepository;
    private final UsernameAPI usernameAPI;
    private final int MAX_ROWS = 6;

    public PunishmentService(PunishmentsRepository punishmentsRepository, UsernameAPI usernameAPI) {
        this.punishmentsRepository = punishmentsRepository;
        this.usernameAPI = usernameAPI;
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
                String username = usernameAPI.usernameLookup(punishment.getVictimUuid());
                punishment.setVictimUsername(username);
            }
        }
    }
}
