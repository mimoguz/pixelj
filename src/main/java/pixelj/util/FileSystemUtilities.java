package pixelj.util;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public final class FileSystemUtilities {

    private FileSystemUtilities() {
    }

    public static OSPair<Path> getConfigDirectory() throws IOException {
        final var os = System.getProperty("os.name").toUpperCase();
        if (os.startsWith("WINDOWS")) {
            return new OSPair<>(OS.WINDOWS, Paths.get(System.getenv("AppData")));
        } else {
            if (os.startsWith("MAC")) {
                final var home = getHome();
                return new OSPair<>(OS.MAC_OS, Paths.get(home, "Library", "Preferences"));
            } else {
                // Assume Linux/Unix
                final var configHome = System.getenv("XDG_CONFIG_HOME");
                final var path = configHome == null || configHome.isBlank()
                    ? Paths.get(getHome(), ".config")
                    : Paths.get(configHome);
                return new OSPair<>(OS.LINUX, path);
            }
        }
    }

    private static String getHome() throws IOException {
        final var home = System.getProperty("user.home");
        if (home == null || home.isBlank()) {
            throw new IOException("Can't get home directory");
        }
        return home;
    }

    public enum OS {
        WINDOWS, LINUX, MAC_OS
    }

    public record OSPair<T>(OS os, T value) {
    }
}
