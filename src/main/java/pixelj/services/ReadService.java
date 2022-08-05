package pixelj.services;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import pixelj.models.CompressedGlyph;
import pixelj.models.CompressedGlyph.MisshapenDataException;
import pixelj.models.Glyph;
import pixelj.models.KerningPair;
import pixelj.models.DocumentSettings;
import pixelj.models.DocumentSettings.Builder.InvalidStateException;
import pixelj.models.Project;
import pixelj.models.SortedList;
import pixelj.services.Queries.GlyphsColumn;
import pixelj.services.Queries.KerningPairsColumn;
import pixelj.services.Queries.SettingsColumn;

final class ReadService {

    private static final String EXTENSION = "." + Queries.EXTENSION;

    private ReadService() {
    }

    public static Project load(final Path path) throws IOException {
        final var pathStr = path.toAbsolutePath().toString();
        if (!path.toFile().exists()) {
            throw new IOException("File not found: " + pathStr);
        }
        final var url = pathStr.endsWith(EXTENSION)
            ? pathStr.substring(0, pathStr.length() - EXTENSION.length())
            : path;
        try (var connection = DriverManager.getConnection(Queries.URL_PREFIX + url, Queries.PIXELJ, "")) {
            final var statement = connection.createStatement();
            final var settings = extractSettings(statement.executeQuery(Queries.SELECT_SETTINGS));
            final var glyphs = extractGlyphs(statement.executeQuery(Queries.SELECT_GLYPHS), settings);
            final var kerningPairs = extractKerningPairs(
                statement.executeQuery(Queries.SELECT_KERNING_PAIRS),
                glyphs
            );
            return new Project(glyphs, kerningPairs, settings, path);
        } catch (SQLException | IOException e) {
            cleanup(path);
            throw new IOException(
                String.format("Can't open or read the file %s:\n%s", path.toString(), e.getMessage())
            );
        } catch (NullPointerException | MisshapenDataException | InvalidStateException e) {
            cleanup(path);
            throw new IOException(
                String.format("%s file is corrupted:\n%s", path.toString(), e.getMessage())
            );
        }

    }

    private static SortedList<KerningPair> extractKerningPairs(
        final ResultSet result,
        final SortedList<Glyph> glyphs
    ) throws SQLException, IllegalArgumentException {
        final var list = new SortedList<KerningPair>();
        while (result.next()) {
            final var left = glyphs.findHash(result.getInt(KerningPairsColumn.LEFT_CODE_POINT.getIndex()));
            final var right = glyphs.findHash(result.getInt(KerningPairsColumn.RIGHT_CODE_POINT.getIndex()));
            if (left == null || right == null) {
                throw new IllegalArgumentException("Left or right glyph is missing");
            }
            list.add(new KerningPair(left, right, result.getInt(4)));
        }
        return list;
    }

    private static SortedList<Glyph> extractGlyphs(final ResultSet result, final DocumentSettings settings)
        throws SQLException, IOException, MisshapenDataException {
        final var list = new SortedList<Glyph>();
        while (result.next()) {
            final var zipped = new CompressedGlyph(
                result.getInt(GlyphsColumn.CODE_POINT.getIndex()),
                result.getInt(GlyphsColumn.WIDTH.getIndex()),
                result.getBinaryStream(GlyphsColumn.IMAGE_BYTES.getIndex()).readAllBytes()
            );
            list.add(zipped.decompress(settings.canvasWidth(), settings.canvasHeight()));
        }
        return list;
    }

    private static DocumentSettings extractSettings(final ResultSet result)
        throws InvalidStateException, SQLException {
        result.next();
        return new DocumentSettings.Builder()
            .setTitle(result.getString(SettingsColumn.TITLE.getIndex()))
            .setCanvasWidth(result.getInt(SettingsColumn.CANVAS_WIDTH.getIndex()))
            .setCanvasHeight(result.getInt(SettingsColumn.CANVAS_HEIGHT.getIndex()))
            .setAscender(result.getInt(SettingsColumn.ASCENDER.getIndex()))
            .setDescender(result.getInt(SettingsColumn.DESCENDER.getIndex()))
            .setCapHeight(result.getInt(SettingsColumn.CAP_HEIGHT.getIndex()))
            .setXHeight(result.getInt(SettingsColumn.X_HEIGHT.getIndex()))
            .setDefaultWidth(result.getInt(SettingsColumn.DEFAULT_WIDTH.getIndex()))
            .setLetterSpacing(result.getInt(SettingsColumn.LETTER_SPACING.getIndex()))
            .setSpaceSize(result.getInt(SettingsColumn.SPACE_SIZE.getIndex()))
            .setLineSpacing(result.getInt(SettingsColumn.LINE_SPACING.getIndex()))
            .setMonospaced(result.getBoolean(SettingsColumn.IS_MONOSPACED.getIndex()))
            .setBold(result.getBoolean(SettingsColumn.IS_BOLD.getIndex()))
            .setItalic(result.getBoolean(SettingsColumn.IS_ITALIC.getIndex()))
            .build();
    }

    // TODO: Investigate: Can I prevent creation of such files in the first place?
    private static void cleanup(final Path path) {
        // Remove *.trace.db
        var name = path.getFileName().toString();
        if (name.endsWith(EXTENSION)) {
            name = name.substring(0, name.length() - EXTENSION.length());
        }
        name += ".trace.db";
        final var tracePath = Paths.get(path.getParent().toAbsolutePath().toString(), name);
        final var trace = tracePath.toFile();
        if (trace.exists()) {
            try {
                trace.delete();
            } catch (SecurityException e) {
                e.printStackTrace();
            }
        }
    }
}
