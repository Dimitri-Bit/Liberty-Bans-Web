package me.dimitri.libertyweb.api;

import jakarta.inject.Provider;
import jakarta.inject.Singleton;
import net.kyori.adventure.audience.MessageType;
import net.kyori.adventure.text.Component;
import org.slf4j.LoggerFactory;
import space.arim.injector.Injector;
import space.arim.libertybans.api.LibertyBans;
import space.arim.libertybans.bootstrap.BaseFoundation;
import space.arim.libertybans.core.database.InternalDatabase;
import space.arim.libertybans.env.standalone.CommandDispatch;
import space.arim.libertybans.env.standalone.ConsoleAudience;
import space.arim.libertybans.env.standalone.ConsoleAudienceToLogger;
import space.arim.libertybans.env.standalone.StandaloneLauncher;
import space.arim.omnibus.DefaultOmnibus;

import java.nio.file.Path;

@Singleton
public class LibertyWeb {

    private final ConsoleAudience consoleAudience;
    private final Injector injector;
    private final LibertyBans api;
    private final BaseFoundation base;
    private final CommandDispatch commandDispatch;

    public LibertyWeb() {
        Path dataFolder = Path.of(System.getProperty("user.dir") + "/LibertyBans/");
        consoleAudience = new ConsoleAudienceToLogger(LoggerFactory.getLogger(LibertyWeb.class));

        injector = new StandaloneLauncher(dataFolder, new DefaultOmnibus())
                .createInjector(consoleAudience);

        api = injector.request(LibertyBans.class);
        base = injector.request(BaseFoundation.class);
        base.startup();
        commandDispatch = injector.request(CommandDispatch.class);

        Provider<InternalDatabase> dbProvider = () -> injector.request(InternalDatabase.class);
        if (!dbProvider.get().getVendor().isRemote()) {
            log("You are not using a remote database. The web interface will not work properly using the embedded database.");
        }
    }

    public void log(String message) {
        consoleAudience.sendMessage(Component.text(message), MessageType.SYSTEM);
    }

    public Injector getInjector() {
        return injector;
    }

    public LibertyBans getApi() {
        return api;
    }

    public BaseFoundation getBase() {
        return base;
    }

    public CommandDispatch getCommandDispatch() {
        return commandDispatch;
    }
}
