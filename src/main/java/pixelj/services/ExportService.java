package pixelj.services;

import java.io.IOException;
import java.nio.file.Path;

import pixelj.models.Project;

public interface ExportService {
    /** File extension. */
    String EXTENSION = "fnt";

    /**
     * Generate fnt file and associated images from project.
     *
     * @param project
     * @param path            Save path
     * @param forceSquare     Image width and height must be equal
     * @param forcePowerOfTwo Image width and height must be powers of two
     * @throws IOException
     */
    void export(Project project, Path path, boolean forceSquare, boolean forcePowerOfTwo) throws IOException;
}
