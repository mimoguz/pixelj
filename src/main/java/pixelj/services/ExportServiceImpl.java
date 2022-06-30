package pixelj.services;

import java.awt.Dimension;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

import javax.imageio.ImageIO;

import pixelj.models.Metrics;
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

    private static StringBuilder infoLine(
            final StringBuilder builder,
            final String title,
            final Metrics metrics
    ) {
        builder.append("info ");

        builder.append(" face=\"");
        builder.append(title);
        builder.append("\"");

        builder.append(" size=");
        builder.append(-metrics.capHeight());

        // TODO: Add isBold and isItalic to export options
        builder.append(" bold=0 italic=0");

        builder.append(" unicode=1 stretchH=100 smooth=0 aa=1 padding=0,0,0,0 spacing=1,1 outline=0");

        return builder;
    }

    private static StringBuilder commonLine(
            final StringBuilder builder,
            final Metrics metrics,
            final Dimension imageSize
    ) {
        builder.append("common");

        builder.append(" lineHeight=");
        builder.append(metrics.ascender() + metrics.descender() + metrics.lineSpacing());

        builder.append(" base=");
        builder.append(metrics.ascender());

        builder.append(" scaleW=");
        builder.append(imageSize.width);
        builder.append(" scaleH=");
        builder.append(imageSize.height);

        // TODO: How to handle multiple pages?
        builder.append(" pages=1");

        builder.append(" packed=0 alphaChnl=0 redChnl=4 greenChnl=4 blueChnl=4");

        return builder;
    }
}
