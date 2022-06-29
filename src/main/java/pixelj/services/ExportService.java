package pixelj.services;

import java.io.IOException;
import java.nio.file.Path;

import pixelj.models.Project;

public interface ExportService {
    String EXTENSION = "fnt";

    void export(Project project, Path path, boolean forceSquare, boolean forcePowerOfTwo) throws IOException;
}
