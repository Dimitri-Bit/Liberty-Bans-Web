package me.dimitri.libertyweb.controllers;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Error;

import java.net.URI;
import java.net.URISyntaxException;

@Controller("/badRequest")
public class BadRequestController {

    @Error(status = HttpStatus.BAD_REQUEST, global = true)
    public HttpResponse<?> notFound(HttpRequest request) throws URISyntaxException {
        URI location = new URI("/400.html");
        return HttpResponse.redirect(location);
    }
}
