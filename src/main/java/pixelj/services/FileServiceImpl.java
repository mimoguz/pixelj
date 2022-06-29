package pixelj.services;

import java.io.IOException;
import java.nio.file.Path;

import org.h2.store.fs.FilePath;

import pixelj.models.Project;

public class FileServiceImpl implements FileService {
    public FileServiceImpl() {
        final var wrapper = new CustomExtensionWrapper();
        FilePath.register(wrapper);
    }

    // TODO: Async save
    public void writeFile(Project project, Path path) throws IOException {
        WriteService.save(path, project);
    }

    public Project readFile(Path path) throws IOException {
        return ReadService.load(path);
    }
}
