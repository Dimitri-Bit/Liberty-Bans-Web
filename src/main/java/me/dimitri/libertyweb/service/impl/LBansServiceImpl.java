package me.dimitri.libertyweb.service.impl;

import io.micronaut.context.annotation.Primary;
import jakarta.inject.Provider;
import jakarta.inject.Singleton;
import lombok.Getter;
import me.dimitri.libertyweb.service.LBansService;
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
import java.nio.file.Files;
import java.nio.file.Path;

@Primary
@Singleton
@Getter
public class LBansServiceImpl implements LBansService {

    private static final String FOLDER_NAME = "libertybans";
    private static final String FOLDER_PATH = System.getProperty("user.dir") + File.separator + FOLDER_NAME;
    private static final Logger logger = LoggerFactory.getLogger(LBansServiceImpl.class);

    private LibertyBans libertyBans;
    private BaseFoundation baseFoundation;

    public boolean setup() {
        Path folderPath = Path.of(FOLDER_PATH);
        if (!folderPath.toFile().exists()) {
            logger.error("You must provide your libertybans folder in the same directory as Liberty Web");
            return false;
        }

        ConsoleAudience consoleAudience = new ConsoleAudienceToLogger(logger);
        Injector injector = new StandaloneLauncher(folderPath, new DefaultOmnibus()).createInjector(consoleAudience);

        libertyBans = injector.request(LibertyBans.class);
        baseFoundation = injector.request(BaseFoundation.class);
        baseFoundation.startup();

        Provider<InternalDatabase> dbProvider = () -> injector.request(InternalDatabase.class);
        if (!dbProvider.get().getVendor().isRemote()) {
            logger.warn("Liberty Web will not work properly unless you are using a remote database. Please switch to MySQL/MariaDB/PostgreSQL");
            return false;
        }

        return true;
    }

}
