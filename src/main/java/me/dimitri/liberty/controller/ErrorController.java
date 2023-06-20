package me.dimitri.liberty.controller;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Error;
import io.micronaut.http.hateoas.JsonError;
import io.micronaut.http.hateoas.Link;

import java.net.URI;

@Controller
public class ErrorController {

    @Error(status = HttpStatus.NOT_FOUND, global = true)
    public HttpResponse<?> notFound(HttpRequest request) {
        if (request.getHeaders().accept().stream().anyMatch(mediaType -> mediaType.getName().contains(MediaType.TEXT_HTML))) {
            return HttpResponse.seeOther(URI.create("/404.html"));
        }

        JsonError error = new JsonError("Page Not Found").link(Link.SELF, Link.of(request.getUri()));
        return HttpResponse.<JsonError>notFound().body(error);
    }

    @Error(status = HttpStatus.BAD_REQUEST, global = true)
    public HttpResponse<?> badRequest(HttpRequest request) {
        if (request.getHeaders().accept().stream().anyMatch(mediaType -> mediaType.getName().contains(MediaType.TEXT_HTML))) {
            return HttpResponse.seeOther(URI.create("/400.html"));
        }

        JsonError error = new JsonError("Bad Request").link(Link.SELF, Link.of(request.getUri()));
        return HttpResponse.<JsonError>notFound().body(error);
    }

}
