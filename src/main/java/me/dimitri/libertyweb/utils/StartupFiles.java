package me.dimitri.libertyweb.utils;

import me.dimitri.libertyweb.utils.exception.FileWorkerException;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Collections;

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
            throw new FileWorkerException("Unable to create config: ", e.getCause());
        }
        return false;
    }

    public boolean createFrontend() throws FileWorkerException {
        try {
            File frontend = new File(rootPath, "frontend");
            if (!frontend.exists()) {
                Files.createDirectories(frontend.toPath());
                copyFromJar("/frontend-src", Paths.get(frontend.toURI()));
                return true;
            }
        } catch (Exception e) {
            throw new FileWorkerException("Unable to create frontend files: ", e.getCause());
        }
        return false;
    }

    // https://stackoverflow.com/a/24316335
    public void copyFromJar(String source, final Path target) throws URISyntaxException, IOException {
        URI resource = getClass().getResource("").toURI();

        try (FileSystem fileSystem = FileSystems.newFileSystem(resource, Collections.<String, String>emptyMap())) {
            final Path jarPath = fileSystem.getPath(source);

            Files.walkFileTree(jarPath, new SimpleFileVisitor<>() {

                @Override
                public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                    Path currentTarget = target.resolve(jarPath.relativize(dir).toString());
                    Files.createDirectories(currentTarget);
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    Files.copy(file, target.resolve(jarPath.relativize(file).toString()), StandardCopyOption.REPLACE_EXISTING);
                    return FileVisitResult.CONTINUE;
                }
            });
        }
    }

    private InputStream getResource(String name) {
        return getClass().getClassLoader().getResourceAsStream(name);
    }

}
