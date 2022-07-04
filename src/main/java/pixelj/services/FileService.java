package pixelj.services;

import java.io.IOException;
import java.nio.file.Path;

import pixelj.models.Project;

public interface FileService {
    /**
     * File extension.
     */
    String EXTENSION = Queries.EXTENSION;

    /**
     * Save the project  to the disk.
     * 
     * @param project
     * @param path
     * @throws IOException
     */
    void writeFile(Project project, Path path) throws IOException;

    /**
     * Load a project from disk.
     * 
     * @param path
     * @return The loaded project
     * @throws IOException
     */
    Project readFile(Path path) throws IOException;
}
