package pixelj.services;

import java.awt.Dimension;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;

import javax.imageio.ImageIO;

import pixelj.models.Glyph;
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

        final var dir = path.getParent();
        final var fileName = path.getFileName().toString();
        final var dotPosition = fileName.lastIndexOf('.');
        final var baseName = dotPosition > 0 ? fileName.substring(0, dotPosition) : fileName;

        var pathStr = path.toAbsolutePath().toString();
        pathStr = pathStr.endsWith("." + EXTENSION)
                ? pathStr.substring(0, pathStr.length() - EXTENSION.length())
                : pathStr;

        ImageIO.write(
                image,
                "png",
                Paths.get(dir.toAbsolutePath().toString(), imageName(0, baseName)).toFile()
        );

        try (
                var writer = new FileWriter(
                        Paths.get(dir.toAbsolutePath().toString(), baseName + "." + EXTENSION).toFile()
                )
        ) {
            fnt(project, baseName, rectangles, imageSize).forEach(block -> {
                try {
                    writer.write(block);
                } catch (IOException e) {
                    throw new RuntimeException(e.getMessage());
                }
            });
        } catch (RuntimeException e) {
            throw new IOException(e.getMessage());
        }
    }

    private static Stream<String> fnt(
            final Project project,
            final String baseName,
            final List<Rectangle> rectangles,
            final Dimension imageSize
    ) {
        final var builder = new StringBuilder();
        infoLine(builder, project.getTitle(), project.getMetrics()).append('\n');
        commonLine(builder, project.getMetrics(), imageSize).append('\n');
        pageLine(builder, 0, baseName).append('\n');
        charsLine(builder, rectangles.size()).append('\n');

        final var headerStream = Stream.of(builder.toString());
        final var charStream = rectangles.stream().map(rect -> {
            final var glyph = project.getGlyphs().findHash(rect.id());
            return characterLineStr(glyph, project.getMetrics(), rect, 0);
        });

        return Stream.concat(headerStream, charStream);
    }

    private static StringBuilder infoLine(
            final StringBuilder builder,
            final String title,
            final Metrics metrics
    ) {
        builder.append("info ");

        builder.append(" face=\"");
        builder.append(title);
        builder.append('"');

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

    private static StringBuilder pageLine(
            final StringBuilder builder,
            final int page,
            final String baseName
    ) {
        builder.append("page");
        builder.append(" id=");
        builder.append(page);
        builder.append(" file=\"");
        builder.append(imageName(page, baseName));
        builder.append('"');
        return builder;
    }

    private static StringBuilder charsLine(final StringBuilder builder, final int charCount) {
        builder.append("chars count=");
        builder.append(charCount);

        return builder;
    }

    private static StringBuilder characterLine(
            final StringBuilder builder,
            final Glyph glyph,
            final Metrics metrics,
            final Rectangle rect,
            final int page
    ) {
        builder.append("char id=");
        builder.append(glyph.getCodePoint());
        builder.append(" x=");
        builder.append(rect.x());
        builder.append(" y=");
        builder.append(rect.y());

        // TODO: Put actual image size info to Rectangle
        builder.append(" width=");
        builder.append(glyph.getWidth());
        builder.append(" height=");
        builder.append(rect.height() - 1);

        // TODO: Put padding info to Rectangle
        builder.append(" xoffset=0 yoffset=0");

        // TODO: Put padding info to Rectangle
        builder.append(" xadvance=");
        builder.append(metrics.letterSpacing());

        builder.append(" page=");
        builder.append(page);

        builder.append(" chnl=15");

        return builder;
    }

    private static String characterLineStr(
            final Glyph glyph,
            final Metrics metrics,
            final Rectangle rect,
            final int page
    ) {
        return characterLine(new StringBuilder(), glyph, metrics, rect, page).append('\n').toString();
    }

    private static String imageName(final int page, final String base) {
        return base + "_" + page + ".png";
    }
}
