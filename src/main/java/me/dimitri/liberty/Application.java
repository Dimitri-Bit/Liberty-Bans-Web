package me.dimitri.liberty;

import io.micronaut.runtime.Micronaut;
import jakarta.annotation.PostConstruct;
import jakarta.inject.Inject;
import me.dimitri.liberty.tasks.Update;
import me.dimitri.liberty.utils.StartupFiles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Application {

    private final Update updateTask;
    private static final Logger log = LoggerFactory.getLogger(Update.class);

    @Inject
    public Application(Update updateTask) {
        this.updateTask = updateTask;
    }

    public static void main(String[] args) {
        StartupFiles startupFiles = new StartupFiles();
        if (startupFiles.createConfig()) {
            log.info(" config.yml created, please configure your Liberty Web application there");
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

    @PostConstruct
    public void startTasks() {
        Thread thread = new Thread(updateTask);
        thread.start();
    }
}