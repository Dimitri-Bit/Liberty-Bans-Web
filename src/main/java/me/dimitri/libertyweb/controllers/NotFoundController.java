package me.dimitri.libertyweb.controllers;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Error;
import io.micronaut.http.hateoas.JsonError;
import io.micronaut.http.hateoas.Link;

import java.net.URI;
import java.net.URISyntaxException;

@Controller("/notfound")
public class NotFoundController {

    @Error(status = HttpStatus.NOT_FOUND, global = true)
    public HttpResponse<?> notFound(HttpRequest request) throws URISyntaxException {
        URI location = new URI("/404.html");
        return HttpResponse.redirect(location);
    }
}
