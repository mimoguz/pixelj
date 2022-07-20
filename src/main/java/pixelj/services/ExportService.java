package pixelj.services;

import java.io.IOException;
import java.nio.file.Path;

import pixelj.models.Project;
import pixelj.views.projectwindow.LayoutStrategy;

public interface ExportService {
    /** File extension. */
    String EXTENSION = "fnt";

    /**
     * Generate fnt file and associated images from project.
     *
     * @param project
     * @param path          Save path
     * @param textureWidth
     * @param textureHeight
     * @param strategy      Packer-rectangle extractor combo
     * @throws IOException
     */
    void export(
            Project project,
            Path path,
            int textureWidth,
            int textureHeight,
            LayoutStrategy strategy
    ) throws IOException;
}
