package me.dimitri.liberty.service;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import me.dimitri.liberty.api.MojangAPI;
import me.dimitri.liberty.model.Punishment;
import me.dimitri.liberty.model.PunishmentsResponse;
import me.dimitri.liberty.repository.HistoryRepository;
import me.dimitri.liberty.utils.PType;

import java.util.List;

@Singleton
public class HistoryService {

    private final HistoryRepository historyRepository;
    private final MojangAPI mojangApi;

    private final int MAX_ROWS = 6;

    @Inject
    public HistoryService(HistoryRepository historyRepository, MojangAPI mojangApi) {
        this.historyRepository = historyRepository;
        this.mojangApi = mojangApi;
    }

    public PunishmentsResponse getHistory(String type, int page) {
        char pType = getType(type);
        if (pType == PType.UNKNOWN.getType()) {
            return null;
        }

        PunishmentsResponse response = historyRepository.query(pType, calculateOffset(page));
        adjustPageCount(response);
        fixUsernames(response.getPunishments());

        return response;
    }

    private int calculateOffset(int page) {
        return (page - 1) * MAX_ROWS;
    }

    private void adjustPageCount(PunishmentsResponse response) {
        int pageCount = response.getPageCount();
        int adjustedPageCount = (int) Math.ceil((double) pageCount / MAX_ROWS);
        response.setPageCount(adjustedPageCount);
    }

    private void fixUsernames(List<Punishment> punishments) {
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