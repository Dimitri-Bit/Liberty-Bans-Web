package me.dimitri.libertyweb;

import io.micronaut.runtime.Micronaut;
import me.dimitri.libertyweb.utils.StartupFiles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Application {

    private static final Logger log = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {
        StartupFiles startupFiles = new StartupFiles();
        if (startupFiles.createConfig()) {
            log.info(" config.yml created, please configure your Liberty Web application there");
            log.info(" Make sure to copy your LibertyBans plugin folder to the same directory as Liberty Web");
            System.exit(0);
            return;
        }
        if (startupFiles.createFrontend()) {
            log.info(" Frontend files for the website have been created, if you wish to edit");
            log.info(" the look of your website you can do so in the \"frontend\" folder located");
            log.info(" in the same directory as your application jar file.");
        }
        System.setProperty("micronaut.config.files", "config.yml");
        Micronaut.build(args).banner(false).start();
    }
}