package pixelj.services;

import java.awt.Dimension;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;
import javax.imageio.ImageIO;
import pixelj.models.DocumentSettings;
import pixelj.models.KerningPair;
import pixelj.models.Project;
import pixelj.util.packer.GridPacker;
import pixelj.util.packer.Rectangle;

// TODO: Which packer? Which ImageWriter? Other params?
// TODO: Make this testable.
// TODO: Not finished yet.
// TODO: Space size?
// TODO: How to handle multiple pages?
public final class ExportServiceImpl implements ExportService {
    @Override
    public void export(
            final Project project,
            final Path path,
            final boolean forceSquare,
            final boolean forcePowerOfTwo
    )
            throws IOException {
        final var settings = project.getDocumentSettings();
        final var rectangles = getRectangles(project);

        // TODO: DI
        final var packer = new GridPacker();
        packer.setRectangles(rectangles);
        final var imageSize = packer.packInPlace(false, false);

        // TODO: DI
        final var image = new BasicImageWriter()
                .getImage(imageSize, rectangles, project.getGlyphs(), settings);

        final var dir = path.getParent();
        final var fileName = path.getFileName().toString();
        final var dotPosition = fileName.lastIndexOf('.');
        final var baseName = dotPosition > 0 ? fileName.substring(0, dotPosition) : fileName;

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
        final var settings = project.getDocumentSettings();
        final var innerHeight = settings.ascender() + settings.descender();
        final var height = innerHeight + 1;
        return project.getGlyphs().getElements().stream().map(glyph -> {
            final var innerWidth = settings.isMonospaced()
                    ? Math.min(glyph.getWidth(), settings.defaultWidth())
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
        final var info = infoLine(project);
        final var common = commonLine(project, imageSize);
        final var page = pageLine(0, baseName);
        final var chars = charsLine(project);
        final var characterLineStream = rectangles.stream()
                .map(rect -> characterLine(project.getDocumentSettings(), rect, 0));
        final var kerningPairLineStream = project.getKerningPairs()
                .getElements()
                .stream()
                .map(ExportServiceImpl::kerningPairLine);

        return Stream
                .of(
                        Stream.of(info),
                        Stream.of(common),
                        Stream.of(page),
                        Stream.of(chars),
                        characterLineStream,
                        kerningPairLineStream
                )
                .flatMap(a -> a);
    }

    private static String infoLine(final Project project) {
        final var title = project.getTitle();
        final var settings = project.getDocumentSettings();
        return new StringBuilder(120 + title.length()).append("info face=\"")
                .append(title)
                .append(" size=")
                .append(-settings.capHeight())
                .append(" bold=")
                .append(settings.isBold() ? 1 : 0)
                .append("  italic=")
                .append(settings.isItalic() ? 1 : 0)
                .append(" unicode=1 stretchH=100 smooth=0 aa=1 padding=0,0,0,0 spacing=1,1 outline=0")
                .toString();
    }

    private static String commonLine(final Project project, final Dimension imageSize) {
        final var settings = project.getDocumentSettings();
        return new StringBuilder(120).append("common lineHeight=")
                .append(settings.ascender() + settings.descender() + settings.lineSpacing())
                .append(" base=")
                .append(settings.ascender())
                .append(" scaleW=")
                .append(imageSize.width)
                .append(" scaleH=")
                .append(imageSize.height)
                .append(" pages=1")
                .append(" packed=0 alphaChnl=0 redChnl=4 greenChnl=4 blueChnl=4")
                .toString();
    }

    private static String pageLine(final int page, final String baseName) {
        return "page id=" + page + " file=\"" + imageName(page, baseName) + '"';
    }

    private static String charsLine(final Project project) {
        return "chars count=" + project.getGlyphs().countWhere(a -> true);
    }

    private static String characterLine(
            final DocumentSettings settings,
            final Rectangle rect,
            final int page
    ) {
        final var advance = (settings.isMonospaced() ? settings.defaultWidth() : rect.innerWidth())
                + settings.letterSpacing();

        return new StringBuilder(100).append("char id=")
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
                .append(" chnl=15")
                .toString();
    }

    private static String kerningPairLine(final KerningPair pair) {
        return new StringBuilder(50).append("kerning first=")
                .append(pair.getLeft().getCodePoint())
                .append(" second=")
                .append(pair.getRight().getCodePoint())
                .append(" amount=")
                .append(pair.getKerningValue())
                .toString();
    }

    private static String imageName(final int page, final String base) {
        return base + "_" + page + ".png";
    }
}
