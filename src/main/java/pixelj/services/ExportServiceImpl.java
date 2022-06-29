package pixelj.services;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

import javax.imageio.ImageIO;

import pixelj.models.Project;
import pixelj.util.packer.GridPacker;
import pixelj.util.packer.Rectangle;

public class ExportServiceImpl implements ExportService {
    // TODO: Which packer? Which ImageWriter? Other params?
    // TODO: Make this testable.
    // TODO: Not finished yet.
    public void export(
            final Project project,
            final Path path,
            final boolean forceSquare,
            final boolean forcePowerOfTwo
    )
            throws IOException {
        final var metrics = project.getMetrics();
        final var rectHeight = metrics.ascender() + metrics.descender();
        final var rectangles = project.getGlyphs().getElements().stream().map(g -> {
            final var rectWidth = (metrics.isMonospaced() ? metrics.defaultWidth() : g.getWidth()) + 1;
            return new Rectangle(g.getCodePoint(), rectWidth, rectHeight);
        }).toList();
        // TODO: DI
        final var packer = new GridPacker();
        packer.setRectangles(rectangles);
        final var imageSize = packer.packInPlace(false, false);
        // TODO: DI
        final var image = new BasicImageWriter()
                .getImage(imageSize, rectangles, project.getGlyphs(), metrics);
        var pathStr = path.toAbsolutePath().toString();
        pathStr = pathStr.endsWith("." + EXTENSION)
                ? pathStr.substring(0, pathStr.length() - EXTENSION.length())
                : pathStr;
        ImageIO.write(image, "png", new File(pathStr + ".png"));
    }
}
