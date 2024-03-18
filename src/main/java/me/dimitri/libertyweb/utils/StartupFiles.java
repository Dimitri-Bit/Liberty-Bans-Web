package me.dimitri.libertyweb.utils;

import me.dimitri.libertyweb.utils.exception.FileWorkerException;
import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;

import java.io.*;
import java.net.URISyntaxException;

public class StartupFiles {
    private final File rootPath;

    public StartupFiles(File rootPath) {

        this.rootPath = rootPath;
    }

    public StartupFiles() {
        this(null);
    }

    public boolean createConfig() throws FileWorkerException {
        try {
            File config = new File(rootPath, "config.yml");
            if (config.createNewFile()) {
                OutputStream outputStream = new FileOutputStream(config);
                outputStream.write(getResource("config.yml").readAllBytes());
                return true;
            }
        } catch (IOException e) {
            throw new FileWorkerException("Unable to create frontend config: ", e.getCause());
        }
        return false;
    }

    public boolean createFrontend() throws FileWorkerException {
        try {
            File frontend = new File(rootPath, "frontend");
            if (!frontend.isDirectory()) {
                File frontendZip = new File("frontend.zip");
                if (frontendZip.createNewFile()) {
                    OutputStream outputStream = new FileOutputStream(frontendZip);
                    outputStream.write(getResource("frontend.zip").readAllBytes());
                }
                unzip(frontendZip);
                return true;
            }
        } catch (Exception e) {
            throw new FileWorkerException("Unable to create frontend files: ", e.getCause());
        }
        return false;
    }

    private void unzip(File file) throws FileWorkerException {
        try {
            ZipFile zipFile = new ZipFile(file);
            zipFile.extractAll(getFilePath());
            file.delete();
        } catch (ZipException | URISyntaxException e) {
            throw new FileWorkerException("Unable to unzip " + file, e.getCause());
        }
    }

    private String getFilePath() throws URISyntaxException {
        String jarPath = StartupFiles.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
        return new File(jarPath).getParent();
    }

    private InputStream getResource(String name) {
        return getClass().getClassLoader().getResourceAsStream(name);
    }

}
