package me.dimitri.libertyweb.listener;

import io.micronaut.context.ApplicationContext;
import io.micronaut.context.event.ShutdownEvent;
import io.micronaut.context.event.StartupEvent;
import io.micronaut.runtime.event.annotation.EventListener;
import jakarta.inject.Singleton;
import me.dimitri.libertyweb.service.LBansService;
import space.arim.libertybans.bootstrap.BaseFoundation;

@Singleton
public class StartupListener {

    private final LBansService lBansService;
    private final ApplicationContext applicationContext;

    public StartupListener(LBansService lBansService, ApplicationContext applicationContext) {
        this.lBansService = lBansService;
        this.applicationContext = applicationContext;
    }

    @EventListener
    public void setupLibertyBansApi(StartupEvent event) {
        boolean success = lBansService.setup();
        if (!success) {
            applicationContext.close();
            System.exit(1);
        }
    }

    @EventListener
    public void shutdownLibertyBansApi(ShutdownEvent event) {
        BaseFoundation baseFoundation = lBansService.getBaseFoundation();
        if (baseFoundation != null)
            baseFoundation.shutdown();
    }

}
