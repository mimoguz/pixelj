package pixelj.services;

import java.io.IOException;
import java.nio.file.Path;

import org.h2.store.fs.FilePath;

import pixelj.models.Project;

// TODO: Async save
/**
 * This implementation uses an h2 database as output file format.
 */
public final class DBFileService implements FileService {
    public DBFileService() {
        final var wrapper = new CustomExtensionWrapper();
        FilePath.register(wrapper);
    }

    @Override
    public void writeFile(final Project project, final Path path) throws IOException {
        WriteService.save(path, project);
    }

    @Override
    public Project readFile(final Path path) throws IOException {
        return ReadService.load(path);
    }
}
