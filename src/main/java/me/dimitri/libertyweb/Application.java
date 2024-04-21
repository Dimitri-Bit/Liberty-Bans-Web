package me.dimitri.libertyweb;

import io.micronaut.runtime.Micronaut;
import me.dimitri.libertyweb.utils.RuntimeConstants;
import me.dimitri.libertyweb.utils.StartupFiles;
import me.dimitri.libertyweb.utils.exception.FileWorkerException;
import me.dimitri.libertyweb.utils.exception.PropertyLoaderException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Application {

    private static final Logger log = LoggerFactory.getLogger(Application.class);

    @SuppressWarnings("Duplicates")
    public static void main(String[] args) {
        StartupFiles startupFiles = new StartupFiles();
        try {
            startupFiles.setRuntimeConstants();
        } catch (PropertyLoaderException e) {
            log.error(e.getMessage());
        }

        String upgradeFrontend = "";
        if (System.getProperty("upgrade_frontend") != null) {
            upgradeFrontend = System.getProperty("upgrade_frontend");
        }

        try {
            if (startupFiles.createConfig()) {
                log.info(" config.yml created, please configure your Liberty Web application there");
                log.info(" Make sure to copy your LibertyBans plugin folder to the same directory as Liberty Web");
                System.exit(0);
                return;
            }
            if (startupFiles.createFrontend(false)) {
                log.info(" Frontend files for the website have been created, if you wish to edit");
                log.info(" the look of your website you can do so in the \"frontend\" folder located");
                log.info(" in the same directory as your application jar file.");
                log.info(" It is recommended to create a new user that has read-only access to your punishments");
                log.info(" database, this will improve overall security and make logging database connections easier.");
            } else {
                // Here we first need to check if it's present at all.
                // If it's not present, an exception will be shallower, and the return value will be an empty string.
                if (startupFiles.checkFrontendVersion().isEmpty()) {
                    RuntimeConstants.setFrontendVersion("2.0.0"); // 2.0.0 was the last version before the frontend wasn't versioned.
                    if (upgradeFrontend.isEmpty()) {
                        log.info(" Your frontend version needs to be updated. The current version is {}, yours is {}.", RuntimeConstants.getBackendVersion(), RuntimeConstants.getFrontendVersion());
                        log.info(" You can update the frontend by adding the -Dupgrade_frontend=true parameter to your startup command.");
                        log.info(" If you have a custom frontend, add the version.json file to the root of the frontend folder, with the current backend version");
                        log.info(" which is always the value of the application version in pom.xml (<version>{}</version>).", RuntimeConstants.getBackendVersion());
                        log.info(" Alternatively, if you would like to supress this message, add the -Dupgrade_frontend=false parameter.");
                    }
                } else {
                    // The response value isn't empty, meaning we have something to compare.
                    RuntimeConstants.setFrontendVersion(startupFiles.checkFrontendVersion());
                    // The two versions don't match, which likely means that the frontend is out of date.
                    if (!RuntimeConstants.getFrontendVersion().equals(RuntimeConstants.getBackendVersion())) {
                        if (upgradeFrontend.isEmpty()) {
                            log.info(" Your frontend version needs to be updated. The current version is {}, yours is {}.", RuntimeConstants.getBackendVersion(), RuntimeConstants.getFrontendVersion());
                            log.info(" Consider updating your frontend code with the -Dupgrade_frontend=true parameter.");
                            log.info(" If you have a custom frontend, add the version.json file to the root of the frontend folder, with the current backend version");
                            log.info(" which is always the value of the application version in pom.xml (<version>{}</version>).", RuntimeConstants.getBackendVersion());
                            log.info(" Alternatively, if you would like to supress this message, add the -Dupgrade_frontend=false parameter.");
                        }
                    }
                }
            }

            if (upgradeFrontend.equals("true")) {
                if (startupFiles.createFrontend(true)) {
                    log.info(" Your frontend files have successfully been updated. Please re-start the application to allow Micronaut to pick up the new files.");
                    System.exit(0);
                    return;
                }
            }

        } catch (FileWorkerException e) {
            log.error(e.getMessage());
            System.exit(0);
            return;
        }
        System.setProperty("micronaut.config.files", "config.yml");
        Micronaut.build(args).banner(false).start();
    }
}