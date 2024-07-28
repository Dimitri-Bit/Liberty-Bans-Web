package me.dimitri.libertyweb.service;

import io.micronaut.context.annotation.Primary;
import jakarta.inject.Provider;
import jakarta.inject.Singleton;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import space.arim.injector.Injector;
import space.arim.libertybans.api.LibertyBans;
import space.arim.libertybans.bootstrap.BaseFoundation;
import space.arim.libertybans.core.database.InternalDatabase;
import space.arim.libertybans.env.standalone.ConsoleAudience;
import space.arim.libertybans.env.standalone.ConsoleAudienceToLogger;
import space.arim.libertybans.env.standalone.StandaloneLauncher;
import space.arim.omnibus.DefaultOmnibus;

import java.io.File;
import java.nio.file.Path;

@Primary
@Singleton
public class LBansServiceImpl {

    private static final String LIBERTY_BANS_FOLDER = System.getProperty("user.dir") + File.pathSeparator + "LibertyBans";
    private static final Logger logger = LoggerFactory.getLogger(LBansServiceImpl.class);

    @Getter
    private LibertyBans libertyBans;

    public void setup() {
        Path path = Path.of(LIBERTY_BANS_FOLDER);
        ConsoleAudience consoleAudience = new ConsoleAudienceToLogger(logger);
        Injector injector = new StandaloneLauncher(path, new DefaultOmnibus()).createInjector(consoleAudience);

        libertyBans = injector.request(LibertyBans.class);

        BaseFoundation baseFoundation = injector.request(BaseFoundation.class);
        baseFoundation.startup();

        Provider<InternalDatabase> dbProvider = () -> injector.request(InternalDatabase.class);
        if (!dbProvider.get().getVendor().isRemote()) {
            logger.warn("Liberty Web will not work properly unless you are using a remote database. Please switch to MySQL/MariaDB/PostgreSQL");
        }
    }

}
