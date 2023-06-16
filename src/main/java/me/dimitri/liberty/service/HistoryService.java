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

    @Inject
    public HistoryService(HistoryRepository historyRepository, MojangAPI mojangApi) {
        this.historyRepository = historyRepository;
        this.mojangApi = mojangApi;
    }

    public PunishmentsResponse getHistory(String punishmentType, int pageNum) {
        int maxRowsPerPage = 6;
        int offset = (pageNum - 1) * maxRowsPerPage;
        char type = getPunishmentType(punishmentType);

        if (type == PType.UNKNOWN.getType()) {
            return null;
        }

        PunishmentsResponse response = historyRepository.queryHistory(type, offset);
        int pageCount = (int) Math.ceil((double) response.getPageCount() / maxRowsPerPage);
        response.setPageCount(pageCount);

        fetchUsernames(response.getPunishments());

        return response;
    }

    private void fetchUsernames(List<Punishment> punishments) {
        for (Punishment punishment : punishments) {
            if (punishment.getVictimUsername().equals("Unknown")) {
                String username = mojangApi.usernameLookup(punishment.getVictimUuid());
                punishment.setVictimUsername(username);
            }
        }
    }

    private char getPunishmentType(String type) {
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