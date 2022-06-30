package pixelj.services;

import java.awt.Dimension;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;

import javax.imageio.ImageIO;

import pixelj.models.KerningPair;
import pixelj.models.Metrics;
import pixelj.models.Project;
import pixelj.util.packer.GridPacker;
import pixelj.util.packer.Rectangle;

public class ExportServiceImpl implements ExportService {
    // TODO: Which packer? Which ImageWriter? Other params?
    // TODO: Make this testable.
    // TODO: Not finished yet.
    // TODO: Space size?
    public void export(
            final Project project,
            final Path path,
            final boolean forceSquare,
            final boolean forcePowerOfTwo
    )
            throws IOException {
        final var metrics = project.getMetrics();
        final var rectangles = getRectangles(project);

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

    private static List<Rectangle> getRectangles(final Project project) {
        final var metrics = project.getMetrics();
        final var innerHeight = metrics.ascender() + metrics.descender();
        final var height = innerHeight + 1;
        return project.getGlyphs().getElements().stream().map(glyph -> {
            final var innerWidth = metrics.isMonospaced() ? Math.min(glyph.getWidth(), metrics.defaultWidth())
                    : glyph.getWidth();
            return new Rectangle(glyph.getCodePoint(), innerWidth + 1, height, innerWidth, innerHeight);
        }).toList();
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
            return characterStr(project.getMetrics(), rect, 0);
        });
        final var kerningPairStream = project.getKerningPairs()
                .getElements()
                .stream()
                .map(ExportServiceImpl::kerningPairStr);
        return Stream.concat(
                Stream.concat(headerStream, charStream),
                Stream.concat(
                        Stream.of("kernings count=" + project.getKerningPairs().getSize() + '\n'),
                        kerningPairStream
                )
        );
    }

    private static StringBuilder infoLine(
            final StringBuilder builder,
            final String title,
            final Metrics metrics
    ) {
        // TODO: Add isBold and isItalic to export options
        builder.append("info face=\"")
                .append(title)
                .append(" size=")
                .append(-metrics.capHeight())
                .append(" bold=0 italic=0")
                .append(" unicode=1 stretchH=100 smooth=0 aa=1 padding=0,0,0,0 spacing=1,1 outline=0");
        return builder;
    }

    private static StringBuilder commonLine(
            final StringBuilder builder,
            final Metrics metrics,
            final Dimension imageSize
    ) {
        // TODO: How to handle multiple pages?
        builder.append("common lineHeight=")
                .append(metrics.ascender() + metrics.descender() + metrics.lineSpacing())
                .append(" base=")
                .append(metrics.ascender())
                .append(" scaleW=")
                .append(imageSize.width)
                .append(" scaleH=")
                .append(imageSize.height)
                .append(" pages=1")
                .append(" packed=0 alphaChnl=0 redChnl=4 greenChnl=4 blueChnl=4");
        return builder;
    }

    private static StringBuilder pageLine(
            final StringBuilder builder,
            final int page,
            final String baseName
    ) {
        builder.append("page id=")
                .append(page)
                .append(" file=\"")
                .append(imageName(page, baseName))
                .append('"');
        return builder;
    }

    private static StringBuilder charsLine(final StringBuilder builder, final int charCount) {
        builder.append("chars count=").append(charCount);
        return builder;
    }

    private static StringBuilder characterLine(
            final StringBuilder builder,
            final Metrics metrics,
            final Rectangle rect,
            final int page
    ) {
        final var advance = (metrics.isMonospaced() ? metrics.defaultWidth() : rect.innerWidth())
                + metrics.letterSpacing();
        builder.append("char id=")
                .append(rect.id())
                .append(" x=")
                .append(rect.x())
                .append(" y=")
                .append(rect.y())
                .append(" width=")
                .append(rect.innerWidth())
                .append(" height=")
                .append(rect.innerHeight())
                .append(" xoffset=0 yoffset=0")
                .append(" xadvance=")
                .append(advance)
                .append(" page=")
                .append(page)
                .append(" chnl=15");
        return builder;
    }

    private static String characterStr(final Metrics metrics, final Rectangle rect, final int page) {
        return characterLine(new StringBuilder(100), metrics, rect, page).append('\n').toString();
    }

    private static StringBuilder kerningPairLine(final StringBuilder builder, final KerningPair pair) {
        builder.append("kerning first=")
                .append(pair.getLeft().getCodePoint())
                .append(" second=")
                .append(pair.getRight().getCodePoint())
                .append(" amount=")
                .append(pair.getKerningValue());
        return builder;
    }

    private static String kerningPairStr(final KerningPair pair) {
        return kerningPairLine(new StringBuilder(60), pair).append('\n').toString();
    }

    private static String imageName(final int page, final String base) {
        return base + "_" + page + ".png";
    }
}
