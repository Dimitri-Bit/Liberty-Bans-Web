package me.dimitri.libertyweb.utils;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import me.dimitri.libertyweb.utils.exception.FileWorkerException;
import me.dimitri.libertyweb.utils.exception.PropertyLoaderException;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Collections;
import java.util.Properties;

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


    // isOverwrite enables us to over-write the currently existing frontend directories with our current one.
    // at some point we really need to add javadoc to these functions.
    public boolean createFrontend(boolean isOverwrite) throws FileWorkerException {
        try {
            File frontend = new File(rootPath, "frontend");
            if (!frontend.exists() || isOverwrite) {
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

    public String checkFrontendVersion()  {
        File fileVersion = new File(rootPath, "frontend/version.json");
        Gson gson = new Gson();
        JsonReader reader;
        String version = "";

        try {
            reader = new JsonReader(new FileReader(fileVersion));
            JsonData data = gson.fromJson(reader, JsonData.class);
            version = data.getVersion();
            return version;
        } catch (FileNotFoundException ignored) { }

        return version;
    }

    public void setRuntimeConstants() throws PropertyLoaderException {
        final Properties properties = new Properties();
        try {
            properties.load(getClass().getClassLoader().getResourceAsStream("backend_version.properties"));
        } catch (IOException e) {
            throw new PropertyLoaderException("Unable to load properties: ", e.getCause());
        }
        RuntimeConstants.setBackendVersion(properties.getProperty("backend_version"));
    }

    private InputStream getResource(String name) {
        return getClass().getClassLoader().getResourceAsStream(name);
    }

}
