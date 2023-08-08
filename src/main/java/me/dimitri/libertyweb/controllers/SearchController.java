package me.dimitri.libertyweb.controllers;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.annotation.Produces;
import me.dimitri.libertyweb.model.response.WebSearchResponse;
import me.dimitri.libertyweb.service.SearchService;

@Controller("/search")
public class SearchController {

    private final SearchService searchService;

    public SearchController(SearchService searchService) {
        this.searchService = searchService;
    }

    @Get("/{username}")
    @Produces(MediaType.APPLICATION_JSON)
    public HttpResponse<?> search(@PathVariable String username) {
        WebSearchResponse webSearchResponse = searchService.searchUser(username);
        if (webSearchResponse != null) {
            return HttpResponse.ok(webSearchResponse);
        }
        return HttpResponse.notFound();
    }
}
