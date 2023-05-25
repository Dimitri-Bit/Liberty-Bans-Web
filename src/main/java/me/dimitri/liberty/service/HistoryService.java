package me.dimitri.liberty.service;

import io.micronaut.cache.annotation.Cacheable;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import me.dimitri.liberty.api.MojangAPI;
import me.dimitri.liberty.model.Punishment;
import me.dimitri.liberty.repository.HistoryRepository;
import me.dimitri.liberty.utils.PType;

import java.util.List;

@Singleton
public class HistoryService {

    private final HistoryRepository historyRepository;
    private final MojangAPI mojangApi;

    @Inject
    public HistoryService(HistoryRepository historyRepository, MojangAPI mojangApi) {
        this.historyRepository = historyRepository;
        this.mojangApi = mojangApi;
    }

    @Cacheable("history-cache")
    public List<Punishment> getHistory(String punishmentType, int pageNum) {
        long start = System.currentTimeMillis();

        int offset = (pageNum - 1) * 6;

        char type = getType(punishmentType);
        if (type == PType.UNKNOWN.getType()) {
            return null;
        }

        List<Punishment> punishments = historyRepository.queryHistory(type, offset);
        checkUsernames(punishments);

        long end = System.currentTimeMillis();
        long time = end - start;

        System.out.println("Time: " + time);

        return punishments;
    }

    /*
    Basically libertybans sometimes doesn't store the username tied to a UUID in the libertybans_latest_names so
    we have to query the name using Mojang's API.

    TODO: Add newly retrieved username to database
     */
    private void checkUsernames(List<Punishment> punishments) {
        for (Punishment punishment : punishments) {
            if (punishment.getVictimUsername().equals("Unknown")) {
                String username = mojangApi.usernameLookup(punishment.getVictimUuid());
                punishment.setVictimUsername(username);
            }
        }
    }

    private char getType(String type) {
        switch (type) {
            case "ban":
                return PType.BAN.getType();
            case "mute":
                return PType.MUTE.getType();
            case "warn":
                return PType.WARN.getType();
            case "kick":
                return PType.KICK.getType();
            default:
                return PType.UNKNOWN.getType();
        }
    }
}