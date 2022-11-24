package pixelj.services;

import pixelj.models.DocumentSettings;
import pixelj.models.Glyph;
import pixelj.util.bmreader2.Tag;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Calendar;
import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SvgExportServiceImpl implements SvgExportService {

    @Override
    public void write(final Path outDir, final Collection<Glyph> glyphs, final DocumentSettings settings) throws
        IOException {
        if (!outDir.toFile().isDirectory()) {
            throw new IOException("Out path is not a directory");
        }
        try {
            getSvgStream(glyphs, settings).forEach(svg -> writeSvg(svg, outDir));
        } catch (IOWrapper wrapper) {
            throw wrapper.getInner();
        }
    }

    @Override
    public void write(
        final Path outDir,
        final Collection<Glyph> glyphs,
        final DocumentSettings settings,
        final String documentName,
        final FontMetadata metadata
    ) throws IOException {
        // TODO: Kerning pairs, space size, line height, integer coordinates.

        if (!outDir.toFile().isDirectory()) {
            throw new IOException("Out path is not a directory");
        }
        final var scriptPath = outDir.resolve(documentName + ".pe");
        final var scriptFile = scriptPath.toFile();
        if (scriptFile.exists()) {
            scriptFile.delete();
        }
        final var script = Files.newOutputStream(scriptPath, StandardOpenOption.CREATE);

        try {
            writeScriptHeader(script, settings, metadata);

            try {
                getSvgStream(glyphs, settings).forEach(svg -> {
                    writeSvg(svg, outDir);
                    writeGlyphScript(script, outDir, svg, settings);
                });
            } catch (IOWrapper wrapper) {
                throw wrapper.getInner();
            }
            script.write("Save(".getBytes(StandardCharsets.UTF_8));
            script.write(
                escape(outDir.resolve(documentName + ".sfd").toAbsolutePath().toString())
                    .getBytes(StandardCharsets.UTF_8)
            );
            script.write(")\n\n".getBytes(StandardCharsets.UTF_8));
            script.close();
        } catch (IOException ex) {
            script.close();
            throw ex;
        }
    }

    private static Stream<Svg> getSvgStream(final Collection<Glyph> glyphs, final DocumentSettings settings) {
        return glyphs.stream().map(g -> new Svg(g, settings));
    }

    private static void writeSvg(final Svg svg, final Path outDir) {
        final var out = outDir.resolve("g" + svg.getId() + ".svg");
        final var file = out.toFile();
        if (file.exists()) {
            file.delete();
        }
        try {
            Files.writeString(out, svg.getXml(), StandardOpenOption.CREATE);
        } catch (IOException ex) {
            throw new IOWrapper(ex);
        }
    }

    private static void writeScriptHeader(
        final OutputStream output,
        final DocumentSettings settings,
        final FontMetadata md
    ) throws IOException {
        output.write("New()\n".getBytes(StandardCharsets.UTF_8));

        final var asc = settings.ascender() * Svg.UNITS_PER_PIXEL;
        final var desc = settings.descender() * Svg.UNITS_PER_PIXEL;
        output.write(("ScaleToEm(" + asc + ", " + desc + ")\n").getBytes(StandardCharsets.UTF_8));

        final var fontName = md.fontName() != null ? md.fontName() : makeName(settings.title());
        final var fullName = md.fullName() != null ? md.fullName() : settings.title();
        final var family = md.familyName() != null ? md.familyName() : fontName;

        var weight = md.weight();
        if (weight == null) {
            if (settings.isBold() && settings.isBold()) {
                weight = "Bold Italic";
            } else if (settings.isBold()) {
                weight = "Bold";
            } else if (settings.isItalic()) {
                weight = "Italic";
            } else {
                weight = "Regular";
            }
        }

        final var copyright = md.copyright() != null
            ? md.copyright()
            : Integer.toString(Calendar.getInstance().get(Calendar.YEAR));

        final var version = md.version() != null ? md.version() : "1.0";

        output.write("SetFontNames(".getBytes(StandardCharsets.UTF_8));
        output.write(escape(fontName).getBytes(StandardCharsets.UTF_8));
        output.write(", ".getBytes(StandardCharsets.UTF_8));
        output.write(escape(family).getBytes(StandardCharsets.UTF_8));
        output.write(", ".getBytes(StandardCharsets.UTF_8));
        output.write(escape(fullName).getBytes(StandardCharsets.UTF_8));
        output.write(", ".getBytes(StandardCharsets.UTF_8));
        output.write(escape(weight).getBytes(StandardCharsets.UTF_8));
        output.write(", ".getBytes(StandardCharsets.UTF_8));
        output.write(escape(copyright).getBytes(StandardCharsets.UTF_8));
        output.write(", ".getBytes(StandardCharsets.UTF_8));
        output.write(escape(version).getBytes(StandardCharsets.UTF_8));
        output.write(")\n".getBytes(StandardCharsets.UTF_8));
        output.write("Reencode(\"unicode\")\n\n".getBytes(StandardCharsets.UTF_8));
    }

    private static void writeGlyphScript(
        final OutputStream output,
        final Path outDir,
        final Svg svg,
        final DocumentSettings settings
    ) {
        try {
            output.write(("Select(" + svg.getId() + ")\n").getBytes(StandardCharsets.UTF_8));
            output.write(("Import(\"" + svgPathString(outDir, svg.getId()) + "\")\n").getBytes(StandardCharsets.UTF_8));
            output.write("Simplify()\n".getBytes(StandardCharsets.UTF_8));
            output.write(("SetWidth(" + svg.getWidth() + ")\n").getBytes(StandardCharsets.UTF_8));
            output.write("SetLBearing(0)\n".getBytes(StandardCharsets.UTF_8));
            output.write(
                ("SetRBearing(" + (settings.letterSpacing() * Svg.UNITS_PER_PIXEL) + ")\n")
                    .getBytes(StandardCharsets.UTF_8)
            );
            output.write(("SetGlyphName(NameFromUnicode(" + svg.getId() + "))\n\n").getBytes(
                StandardCharsets.UTF_8));
        } catch (IOException ex) {
            throw new IOWrapper(ex);
        }
    }

    private static Path svgPath(final Path outDir, final int id) {
        return outDir.resolve("g" + id + ".svg");
    }

    private static String svgPathString(final Path outDir, final int id) {
        return svgPath(outDir, id).toAbsolutePath().toString().replace('\\', '/');
    }

    private static String escape(final String str) {
        return "\"" + str.replace("\"", "\\\"").replace("\\", "\\\\") + '"';
    }

    private static String makeName(final String title) {
        return title.replaceAll("[^A-Z^a-z^0-9]+", "");
    }

    private static class IOWrapper extends RuntimeException {
        private final IOException inner;

        IOWrapper(final IOException ex) {
            super(ex);
            inner = ex;
        }

        public IOException getInner() {
            return inner;
        }
    }
}
