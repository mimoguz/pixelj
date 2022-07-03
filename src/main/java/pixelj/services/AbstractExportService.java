package pixelj.services;

import java.awt.image.BufferedImage;
import java.awt.Dimension;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import javax.imageio.ImageIO;

import pixelj.models.DocumentSettings;
import pixelj.models.KerningPair;
import pixelj.models.Project;
import pixelj.util.packer.Rectangle;

// TODO: Which packer? Which ImageWriter? Other params? Use DI instead of an abstract class.
// TODO: Make this testable.
// TODO: Not finished yet.
// TODO: Space size?
public abstract class AbstractExportService implements ExportService {
    @Override
    public final void export(final Project project, final Path path, final int textureWidth,
            final int textureHeight) throws IOException {
        final var settings = project.getDocumentSettings();
        final var glyphs = project.getGlyphs();
        final var packedRectangles = pack(project, textureWidth, textureHeight);

        // TODO: DI
        // Get images
        final var imageWriter = new BasicImageWriter();
        final var imageSize = new Dimension(textureWidth, textureHeight);
        final var images =
                IntStream.range(0, packedRectangles.size()).parallel().mapToObj(index -> {
                    final var segment = packedRectangles.get(index);
                    return new PageImage(index,
                            imageWriter.getImage(imageSize, segment, glyphs, settings));
                });

        // Get out path
        final var dir = path.getParent();
        final var dirStr = dir.toAbsolutePath().toString();
        final var fileName = path.getFileName().toString();
        final var dotPosition = fileName.lastIndexOf('.');
        final var baseName = dotPosition > 0 ? fileName.substring(0, dotPosition) : fileName;

        try {
            images.forEach(img -> {
                try {
                    ImageIO.write(img.image, "png",
                            new File(Paths.get(dirStr, imageName(img.page, baseName)).toString()));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        } catch (RuntimeException e) {
            throw new IOException(e.getMessage(), e.getCause());
        }

        try (var writer = new FileWriter(
                Paths.get(dir.toAbsolutePath().toString(), baseName + "." + EXTENSION).toFile())) {
            fnt(project, baseName, packedRectangles, imageSize).forEach(block -> {
                try {
                    writer.write(block);
                    writer.write('\n');
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        } catch (RuntimeException e) {
            throw new IOException(e.getMessage(), e.getCause());
        }
    }

    protected abstract List<List<Rectangle>> pack(Project project, int textureWidth,
            int textureHeight);

    protected static Stream<String> fnt(final Project project, final String baseName,
            final List<List<Rectangle>> rectangles, final Dimension imageSize) {
        final var info = infoLine(project);
        final var common = commonLine(project, imageSize, rectangles.size());
        final var chars = charsLine(project);
        final var pages = pageStream(rectangles, baseName);
        final var characters = characterStream(rectangles, project.getDocumentSettings());
        final var kerningPairs = project.getKerningPairs().getElements().stream()
                .map(AbstractExportService::kerningPairLine);
        return Stream.of(Stream.of(info), Stream.of(common), pages, Stream.of(chars), characters,
                kerningPairs).flatMap(a -> a);
    }

    protected static String infoLine(final Project project) {
        final var title = project.getTitle();
        final var settings = project.getDocumentSettings();
        return new StringBuilder(120 + title.length()).append("info face=\"").append(title)
                .append(" size=").append(-settings.capHeight()).append(" bold=")
                .append(settings.isBold() ? 1 : 0).append("  italic=")
                .append(settings.isItalic() ? 1 : 0)
                .append(" unicode=1 stretchH=100 smooth=0 aa=1 padding=0,0,0,0 spacing=1,1 outline=0")
                .toString();
    }

    protected static String commonLine(final Project project, final Dimension imageSize,
            final int pageCount) {
        final var settings = project.getDocumentSettings();
        return new StringBuilder(120).append("common lineHeight=")
                .append(settings.ascender() + settings.descender() + settings.lineSpacing())
                .append(" base=").append(settings.ascender()).append(" scaleW=")
                .append(imageSize.width).append(" scaleH=").append(imageSize.height)
                .append(" pages=").append(pageCount)
                .append(" packed=0 alphaChnl=0 redChnl=4 greenChnl=4 blueChnl=4").toString();
    }

    protected static String pageLine(final int page, final String baseName) {
        return "page id=" + page + " file=\"" + imageName(page, baseName) + '"';
    }

    protected static String charsLine(final Project project) {
        return "chars count=" + project.getGlyphs().countWhere(a -> true);
    }

    protected static String characterLine(final DocumentSettings settings, final Rectangle rect,
            final int page) {
        final var advance = (settings.isMonospaced() ? settings.defaultWidth() : rect.innerWidth())
                + settings.letterSpacing();

        return new StringBuilder(100).append("char id=").append(rect.id()).append(" x=")
                .append(rect.x()).append(" y=").append(rect.y()).append(" width=")
                .append(rect.innerWidth()).append(" height=").append(rect.innerHeight())
                .append(" xoffset=0 yoffset=0").append(" xadvance=").append(advance)
                .append(" page=").append(page).append(" chnl=15").toString();
    }

    protected static String kerningPairLine(final KerningPair pair) {
        return new StringBuilder(50).append("kerning first=").append(pair.getLeft().getCodePoint())
                .append(" second=").append(pair.getRight().getCodePoint()).append(" amount=")
                .append(pair.getKerningValue()).toString();
    }

    protected static Stream<String> pageStream(final List<List<Rectangle>> rectangles,
            final String baseName) {
        final var pageCount = rectangles.size();
        return IntStream.range(0, pageCount).mapToObj(page -> pageLine(page, baseName));
    }

    protected static Stream<String> characterStream(final List<List<Rectangle>> rectangles,
            final DocumentSettings settings) {
        final var pageCount = rectangles.size();
        return IntStream.range(0, pageCount).mapToObj(page -> rectangles.get(page).stream()
                .map(rect -> characterLine(settings, rect, page))).flatMap(a -> a);
    }

    protected static String imageName(final int page, final String base) {
        return base + "_" + page + ".png";
    }

    private record PageImage(int page, BufferedImage image) {
    }
}
