package pixelj.services;

import java.nio.file.Path;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.h2.store.fs.FilePath;

import pixelj.models.Project;

public class DBService {
    private static final String URL_PREFIX = "jdbc:h2:";
    private static final String PIXELJ = "pixelj";

    static {
        final var wrapper = new CustomExtensionWrapper(PIXELJ, PIXELJ);
        FilePath.register(wrapper);
    }

    public static boolean writeFile(Project project, Path path) {
        try {
            final var connection = DriverManager
                    .getConnection(URL_PREFIX + PIXELJ + ":" + path.toAbsolutePath().toString(), PIXELJ, "");
            // TODO: Saving logic
            connection.close();
        } catch (SQLException exception) {
            return false;
        }
        return true;
    }

    public static Project loadFile(Path path) {
        return null;
    }
}
