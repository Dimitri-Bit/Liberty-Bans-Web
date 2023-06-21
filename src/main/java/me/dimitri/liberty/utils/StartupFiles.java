package me.dimitri.liberty.utils;

import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;

import java.io.*;
import java.net.URISyntaxException;

public class StartupFiles {

    public boolean createConfig() {
        try {
            File config = new File("config.yml");
            if (config.createNewFile()) {
                OutputStream outputStream = new FileOutputStream(config);
                outputStream.write(getResource("config.yml").readAllBytes());
                return true;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    public boolean createFrontend() {
        try {
            File frontend = new File("frontend");
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
            throw new RuntimeException(e);
        }
        return false;
    }

    private void unzip(File file) {
        try {
            ZipFile zipFile = new ZipFile(file);
            zipFile.extractAll(getFilePath());
            file.delete();
        } catch (ZipException | URISyntaxException e) {
            throw new RuntimeException(e);
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
