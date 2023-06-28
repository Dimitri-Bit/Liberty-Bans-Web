package me.dimitri.liberty;

import io.micronaut.runtime.Micronaut;
import jakarta.annotation.PostConstruct;
import jakarta.inject.Inject;
import me.dimitri.liberty.tasks.Update;
import me.dimitri.liberty.utils.StartupFiles;

public class Application {

    private final Update updateTask;

    @Inject
    public Application(Update updateTask) {
        this.updateTask = updateTask;
    }

    public static void main(String[] args) {
        StartupFiles startupFiles = new StartupFiles();
        if (startupFiles.createConfig()) {
            printSpace();
            System.out.println(" config.yml created, please configure your Liberty Web application there");
            System.out.println();
            System.out.println(" Please make sure to run your application with the following command:");
            System.out.println(" -> java -jar -Dmicronaut.config.files=\"config.yml\" -jar application.jar");
            System.exit(0);
            return;
        }

        if (startupFiles.createFrontend()) {
            printSpace();
            System.out.println(" Frontend files for the website have been created, if you wish to edit");
            System.out.println(" the look of your website you can do so in the \"frontend\" folder located");
            System.out.println(" in the same directory as your application jar file.");
        }

        Micronaut.build(args).banner(false).start();
    }

    private static void printSpace() {
        for (int i = 0; i < 55; i++) {
            System.out.println();
        }
    }

    @PostConstruct
    public void startTasks() {
        Thread thread = new Thread(updateTask);
        thread.start();
    }
}