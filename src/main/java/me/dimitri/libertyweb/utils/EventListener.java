package me.dimitri.libertyweb.utils;

import io.micronaut.runtime.server.event.ServerShutdownEvent;
import jakarta.inject.Inject;
import me.dimitri.libertyweb.api.LibertyWeb;

public class EventListener {
    private final LibertyWeb libertyWeb;

    @Inject
    public EventListener(LibertyWeb libertyWeb) {
        this.libertyWeb = libertyWeb;
    }

    @io.micronaut.runtime.event.annotation.EventListener
    @SuppressWarnings("unused")
    public void onServerShutDownEvent(ServerShutdownEvent event) {
        libertyWeb.getBase().shutdown();
    }


}
