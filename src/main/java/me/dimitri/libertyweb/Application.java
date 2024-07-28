package me.dimitri.libertyweb;

import io.micronaut.runtime.Micronaut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Application {

    private static final Logger log = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {
        System.setProperty("micronaut.config.files", "config.yml");
        Micronaut.build(args).banner(false).start();
    }
}