package pixelj.services;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

import javax.imageio.ImageIO;

import pixelj.models.Project;
import pixelj.util.packer.GridPacker;
import pixelj.util.packer.Packer;
import pixelj.util.packer.Rectangle;

public class ExportService {

    // TODO: Which packer? Which ImageWriter? Other params?
    // TODO: Make this testable.
    // TODO: Not finished yet.
    public static void export(final Project project, final Path path) throws IOException {
        final var metrics = project.getMetrics();
        final var rectHeight = metrics.ascender() + metrics.descender();
        final var rectangles = project.getGlyphs().getElements().stream().map(g -> {
            final var rectWidth = (metrics.isMonospaced() ? metrics.defaultWidth() : g.getWidth()) + 1;
            return new Rectangle(g.getCodePoint(), rectWidth, rectHeight);
        }).toList();
        final var packer = new GridPacker(rectangles);
        final var imageSize = packer.packInPlace(false, false);
        final var image = new SingleThreadedImageWriter()
                .getImage(imageSize, rectangles, project.getGlyphs());
        final var pathStr = path.toAbsolutePath().toString();
        // if (!pathStr.endsWith(".png")) {
        // pathStr += ".png";
        // }
        ImageIO.write(image, "png", new File(pathStr));
    }
}
