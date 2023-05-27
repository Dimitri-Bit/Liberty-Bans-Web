package me.dimitri.liberty.model;

import java.util.ArrayList;
import java.util.List;

public class PunishmentsResponse {

    private int pageCount;
    private List<Punishment> punishments = new ArrayList<>();

    public PunishmentsResponse() {
        super();
    }

    public PunishmentsResponse(int pageCount, List<Punishment> punishments) {
        this.pageCount = pageCount;
        this.punishments = punishments;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public List<Punishment> getPunishments() {
        return punishments;
    }

    public void setPunishments(List<Punishment> punishments) {
        this.punishments = punishments;
    }
}
