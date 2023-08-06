package me.dimitri.libertyweb.controllers;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.annotation.Produces;
import me.dimitri.libertyweb.model.response.StatisticsResponse;
import me.dimitri.libertyweb.service.StatisticsService;

@Controller("/stats")
public class StatisticsController {

    private final StatisticsService statisticsService;

    public StatisticsController(StatisticsService statisticsService) {
        this.statisticsService = statisticsService;
    }

    @Get("/{type}")
    @Produces(MediaType.APPLICATION_JSON)
    public HttpResponse<?> getStats(@PathVariable String type) {
        StatisticsResponse response = statisticsService.getStatistics(type);
        return HttpResponse.ok().body(response);
    }
}
