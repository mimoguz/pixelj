package pixelj.services;

import java.io.IOException;
import java.nio.file.Path;

import org.h2.store.fs.FilePath;

import pixelj.models.Project;

public class FileService {
    public static final String EXTENSION = Queries.EXTENSION;

    static {
        final var wrapper = new CustomExtensionWrapper();
        FilePath.register(wrapper);
    }

    // TODO: Async save
    public static boolean writeFile(Project project, Path path) throws IOException {
        return SaveService.save(path, project);
    }

    public static Project loadFile(Path path) throws Exception {
        return LoadService.load(path);
    }
}
