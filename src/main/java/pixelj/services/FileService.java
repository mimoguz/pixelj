package pixelj.services;

import java.io.IOException;
import java.nio.file.Path;

import pixelj.models.Project;

public interface FileService {
    String EXTENSION = Queries.EXTENSION;

    void writeFile(Project project, Path path) throws IOException;

    Project readFile(Path path) throws IOException;
}
