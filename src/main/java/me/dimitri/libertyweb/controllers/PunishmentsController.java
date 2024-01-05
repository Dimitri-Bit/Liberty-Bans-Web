package me.dimitri.libertyweb.controllers;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.annotation.Produces;
import jakarta.inject.Inject;
import me.dimitri.libertyweb.model.response.WebPunishmentResponse;
import me.dimitri.libertyweb.service.PunishmentService;

@Controller("/punishments")
public class PunishmentsController {

    private final PunishmentService punishmentService;

    @Inject
    public PunishmentsController(PunishmentService punishmentService) {
        this.punishmentService = punishmentService;
    }

    @Get("/{type}/{page}")
    @Produces(MediaType.APPLICATION_JSON)
    public HttpResponse<?> getHistory(@PathVariable String type, @PathVariable String page) {
        WebPunishmentResponse response = punishmentService.getPunishments(type, page);
        if (response != null) {
            return HttpResponse.ok().body(response);
        }
        return HttpResponse.badRequest();
    }
}
