package pixelj.services;

import pixelj.graphics.BinaryImage;
import pixelj.models.DocumentSettings;
import pixelj.models.Glyph;
import pixelj.models.KerningPair;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Collection;
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
    public void writeWithScript(
        final Path outDir,
        final Collection<Glyph> glyphs,
        final Collection<KerningPair> kerningPairs,
        final DocumentSettings settings,
        final String documentName
    ) throws IOException {
        // TODO: integer coordinates.

        if (!outDir.toFile().isDirectory()) {
            throw new IOException("Out path is not a directory");
        }

        final var scriptPath = outDir.resolve(documentName + ".pe");
        final var scriptFile = scriptPath.toFile();
        if (scriptFile.exists()) {
            scriptFile.delete();
        }

        try (var script = Files.newOutputStream(scriptPath, StandardOpenOption.CREATE)) {

            writeScriptHeader(script, settings);

            try {
                getSvgStream(glyphs, settings).forEach(svg -> {
                    writeSvg(svg, outDir);
                    writeGlyphScript(script, outDir, svg, settings);
                });
            } catch (IOWrapper wrapper) {
                throw wrapper.getInner();
            }

            writeSpace(script, settings);

            for (var pair : kerningPairs) {
                writeKerningPairScript(script, pair);
            }

            script.write("Save(".getBytes(StandardCharsets.UTF_8));
            script.write(
                escape(outDir.resolve(documentName + ".sfd").toAbsolutePath().toString())
                    .getBytes(StandardCharsets.UTF_8)
            );
            script.write(")\n\n".getBytes(StandardCharsets.UTF_8));
        } catch (IOWrapper ex) {
            throw ex.getInner();
        }
    }

    private static Stream<Svg> getSvgStream(final Collection<Glyph> glyphs, final DocumentSettings settings) {
        return glyphs.stream().map(g -> new Svg(g, settings));
    }

    private static void writeSpace(final OutputStream output, final DocumentSettings settings) throws IOException {
        final var width = settings.isMonospaced()
            ? settings.defaultWidth() + settings.letterSpacing()
            : settings.spaceSize();
        writeStrLn(output, "Select(32)");
        writeStrLn(output, "SetWidth(0)");
        writeStrLn(output, "SetLBearing(0)");
        writeStrLn(output, "SetRBearing(", width * Svg.UNITS_PER_PIXEL, ')');
        writeStrLn(output, "SetGlyphName(NameFromUnicode(32))\n");
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

    private static void writeScriptHeader(final OutputStream output, final DocumentSettings settings) throws
        IOException {
        writeStrLn(output, "New()");

        final var asc = settings.ascender() * Svg.UNITS_PER_PIXEL;
        final var desc = (settings.descender() + settings.lineSpacing()) * Svg.UNITS_PER_PIXEL;
        final var weight = settings.isBold() ? "\"Bold\"" : "\"Regular\"";
        writeStrLn(output, "ScaleToEm(", asc, ", ", desc, ')');
        writeStrLn(
            output,
            "SetFontNames(",
            escape(makeName(settings.title())),
            ", ",
            escape(settings.title()),
            ", ",
            escape(settings.title()),
            ", ",
            weight,
            ')'
        );
        writeStrLn(output, "AddSizeFeature(", settings.ascender(), ')');
        writeStrLn(output, "Reencode(\"unicode\")\n");
    }

    private static void writeGlyphScript(
        final OutputStream output,
        final Path outDir,
        final Svg svg,
        final DocumentSettings settings
    ) {
        try {
            writeStrLn(output, "Select(", svg.getId(), ')');
            writeStrLn(output, "Import(\"", svgPathString(outDir, svg.getId()), "\")");
            // TODO: Running simplify causes non-integer coordinates. Investigate.
            // writeStrLn(output, "Simplify()");
            writeStrLn(output, "RoundToInt(", Svg.UNITS_PER_PIXEL, ')');
            writeStrLn(output, "SetWidth(", svg.getWidth(), ')');
            writeStrLn(output, "SetLBearing(0)");
            writeStrLn(output, "SetRBearing(", settings.letterSpacing() * Svg.UNITS_PER_PIXEL, ')');
            writeStrLn(output, "SetGlyphName(NameFromUnicode(", svg.getId(), "))\n");
        } catch (IOException ex) {
            throw new IOWrapper(ex);
        }
    }

    private static void writeKerningPairScript(final OutputStream output, final KerningPair pair) throws IOException {
        writeStrLn(output, "Select(", pair.getLeft().getCodePoint(), ')');
        writeStrLn(
            output,
            "SetKern(",
            pair.getRight().getCodePoint(),
            ", ",
            pair.getKerningValue() * Svg.UNITS_PER_PIXEL,
            ")\n"
        );
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

    private static void writeStrLn(final OutputStream out, final String str) throws IOException {
        out.write(str.getBytes(StandardCharsets.UTF_8));
        out.write('\n');
    }

    private static void writeStrLn(final OutputStream out, final Object... objects) throws IOException {
        for (var obj : objects) {
            out.write(obj.toString().getBytes(StandardCharsets.UTF_8));
        }
        out.write('\n');
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
