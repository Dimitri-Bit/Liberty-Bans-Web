package me.dimitri.liberty.controller;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.annotation.Produces;
import jakarta.inject.Inject;
import me.dimitri.liberty.model.PunishmentsResponse;
import me.dimitri.liberty.service.HistoryService;

@Controller("/history")
public class HistoryController {

    private final HistoryService historyService;

    @Inject
    public HistoryController(HistoryService historyService) {
        this.historyService = historyService;
    }

    @Get("/{type}")
    @Produces(MediaType.APPLICATION_JSON)
    public HttpResponse<?> getHistory(@PathVariable String type) {
        PunishmentsResponse response = historyService.getHistory(type, 1);

        if (response != null) {
            return HttpResponse.ok().body(response);
        }
        return HttpResponse.badRequest();
    }

}
