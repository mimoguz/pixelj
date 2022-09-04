package pixelj.services;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import javax.imageio.ImageIO;
import javax.print.Doc;

import pixelj.models.DocumentSettings;
import pixelj.models.Glyph;
import pixelj.models.KerningPair;
import pixelj.models.Project;
import pixelj.util.IOExceptionWrapper;
import pixelj.util.packer.GridPacker;
import pixelj.util.packer.Packer;
import pixelj.util.packer.Rectangle;
import pixelj.util.packer.RowPacker;
import pixelj.views.projectwindow.LayoutStrategy;

// TODO: How can I make the export method testable?
// TODO: Not finished yet.
public final class ExportServiceImpl implements ExportService {
    private final ImageWriter imageWriter;

    public ExportServiceImpl(final ImageWriter imageWriter) {
        this.imageWriter = imageWriter;
    }

    @Override
    public void export(
        final Project project,
        final Path path,
        final int textureWidth,
        final int textureHeight,
        final LayoutStrategy strategy
    ) throws IOException {

        final var imageSize = new Dimension(textureWidth, textureHeight);
        final var baseName = extensionRemoved(path.getFileName().toString());

        final List<List<Rectangle<GlyphImageData>>> packedRectangles;
        final Stream<String> config;
        final Stream<PageImage> images;

        synchronized (project) {
            packedRectangles = pack(project, textureWidth, textureHeight, strategy);
            config = fnt(project, baseName, packedRectangles, imageSize);
            images = IntStream.range(0, packedRectangles.size())
                .parallel()
                .mapToObj(index -> {
                    final var page = packedRectangles.get(index);
                    return new PageImage(index, imageWriter.getImage(
                        imageSize,
                        page,
                        project.getGlyphs(),
                        project.getDocumentSettings()
                    ));
                });
        }

        // Get out path
        final var dir = path.getParent();
        final var dirStr = dir.toAbsolutePath().toString();

        // Save images
        try {
            images.forEach(img -> {
                try {
                    final var file = Paths.get(dirStr, imageName(img.page, baseName)).toFile();
                    ImageIO.write(img.image, "png", file);
                } catch (IOException e) {
                    throw new IOExceptionWrapper(e);
                }
            });
        } catch (IOExceptionWrapper e) {
            throw new IOException(e.getMessage(), e.getCause());
        }

        // Create and save fnt file
        try (var writer = new OutputStreamWriter(
            new FileOutputStream(Paths.get(dirStr, baseName + "." + EXTENSION).toFile()),
            StandardCharsets.UTF_8
        )) {
            config.forEach(block -> {
                try {
                    writer.write(block);
                    writer.write('\n');
                } catch (IOException e) {
                    throw new IOExceptionWrapper(e);
                }
            });
        } catch (IOExceptionWrapper e) {
            throw new IOException(e);
        }
    }

    private static String extensionRemoved(final String fileName) {
        final var dotPosition = fileName.lastIndexOf('.');
        return dotPosition > 0 ? fileName.substring(0, dotPosition) : fileName;
    }

    private List<List<Rectangle<GlyphImageData>>> pack(
        final Project project,
        final int textureWidth,
        final int textureHeight,
        final LayoutStrategy strategy
    ) {
        final var settings = project.getDocumentSettings();
        final var elements = new ArrayList<>(project.getGlyphs().getElements());

        // Insert space
        final var spaceSize = settings.spaceSize() - settings.letterSpacing();
        elements.add(new Glyph(32, Math.max(spaceSize, 0), null));

        final Stream<Rectangle<GlyphImageData>> rectangleStream;
        final Packer<GlyphImageData> packer;
        switch (strategy) {
            case GRID_LAYOUT:
                rectangleStream = elements
                    .stream()
                    .map(glyph -> GlyphImageData.findLoose(glyph, settings));
                packer = new GridPacker<>();
                break;
            default: // ROW_LAYOUT
                rectangleStream = elements
                    .stream()
                    .map(glyph -> GlyphImageData.findFitting(glyph, settings));
                packer = new RowPacker<>();
                break;
        }

        final var rectangles = rectangleStream
            .filter(rect -> rect.getMetadata() != null)
            .collect(Collectors.toCollection(ArrayList::new));

        return packer.pack(rectangles, textureWidth, textureHeight);
    }

    private static Stream<String> fnt(
        final Project project,
        final String baseName,
        final List<List<Rectangle<GlyphImageData>>> rectangles,
        final Dimension imageSize
    ) {
        final var info = infoLine(project);
        final var common = commonLine(project, imageSize, rectangles.size());
        final var chars = charsLine(project);
        final var pages = pageStream(rectangles, baseName);
        final var characters = characterStream(rectangles, project.getDocumentSettings());
        final var kerningPairs = kerningPairStream(project);

        return Stream.of(
            Stream.of(info),
            Stream.of(common),
            pages,
            Stream.of(chars),
            characters,
            kerningPairs
        ).flatMap(a -> a);
    }

    private static String infoLine(final Project project) {
        final var title = project.getTitle();
        final var settings = project.getDocumentSettings();
        return new StringBuilder(120 + title.length())
            .append("info face=\"")
            .append(title)
            .append("\" size=")
            .append(-settings.capHeight())
            .append(" bold=")
            .append(settings.isBold() ? 1 : 0)
            .append(" italic=")
            .append(settings.isItalic() ? 1 : 0)
            .append(" unicode=1 stretchH=100 smooth=0 aa=1 padding=0,0,0,0 spacing=1,1 outline=0")
            .toString();
    }

    private static String commonLine(
        final Project project,
        final Dimension imageSize,
        final int pageCount
    ) {
        final var settings = project.getDocumentSettings();
        return new StringBuilder(120)
            .append("common lineHeight=")
            .append(settings.ascender() + settings.descender() + settings.lineSpacing())
            .append(" base=")
            .append(settings.ascender())
            .append(" scaleW=")
            .append(imageSize.width)
            .append(" scaleH=")
            .append(imageSize.height)
            .append(" pages=")
            .append(pageCount)
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
        final Rectangle<GlyphImageData> rect,
        final int page
    ) {
        final var md = rect.getMetadata();
        return new StringBuilder(100)
            .append("char id=")
            .append(rect.getId())
            .append(" x=")
            .append(rect.getX())
            .append(" y=")
            .append(rect.getY())
            .append(" width=")
            .append(md.clipWidth())
            .append(" height=")
            .append(md.clipHeight())
            .append(" xoffset=")
            .append(md.xOffset())
            .append(" yoffset=")
            .append(md.yOffset())
            .append(" xadvance=")
            .append(md.glyphWidth() + settings.letterSpacing())
            .append(" page=")
            .append(page)
            .append(" chnl=15")
            .toString();
    }

    private static String kerningPairLine(final KerningPair pair) {
        return new StringBuilder(50)
            .append("kerning first=")
            .append(pair.getLeft().getCodePoint())
            .append(" second=")
            .append(pair.getRight().getCodePoint())
            .append(" amount=")
            .append(pair.getKerningValue())
            .toString();
    }

    private static Stream<String> pageStream(
        final List<List<Rectangle<GlyphImageData>>> rectangles,
        final String baseName
    ) {
        final var pageCount = rectangles.size();
        return IntStream.range(0, pageCount).mapToObj(page -> pageLine(page, baseName));
    }

    private static Stream<String> characterStream(
        final List<List<Rectangle<GlyphImageData>>> rectangles,
        final DocumentSettings settings
    ) {
        final var pageCount = rectangles.size();
        return IntStream.range(0, pageCount)
                .mapToObj(page ->
                        rectangles.get(page)
                                .stream()
                                .map(rect -> characterLine(settings, rect, page))
                )
                .flatMap(a -> a);
    }

    private static Stream<String> kerningPairStream(final Project project) {
        if (project.getDocumentSettings().isMonospaced()) {
            // Ignore kerning pairs for monospaced fonts.
            return Stream.<String>empty();
        }
        return project.getKerningPairs().getElements().stream().map(ExportServiceImpl::kerningPairLine);
    }

    private static String imageName(final int page, final String base) {
        return base + "_" + page + ".png";
    }

    private record PageImage(int page, BufferedImage image) {
    }
}
