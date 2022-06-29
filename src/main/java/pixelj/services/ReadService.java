package pixelj.services;

import java.io.IOException;
import java.nio.file.Path;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import pixelj.models.CompressedGlyph;
import pixelj.models.CompressedGlyph.MisshapenDataException;
import pixelj.models.Glyph;
import pixelj.models.KerningPair;
import pixelj.models.Metrics;
import pixelj.models.Metrics.ValidatedBuilder.InvalidStateException;
import pixelj.models.Project;
import pixelj.models.SortedList;
import pixelj.services.Queries.GlyphsColumn;
import pixelj.services.Queries.KerningPairsColumn;
import pixelj.services.Queries.MetricsColumn;
import pixelj.services.Queries.TitleColumn;

class ReadService {
    public static Project load(final Path path) throws IOException {
        final var pathStr = path.toAbsolutePath().toString();
        final var url = pathStr.endsWith("." + Queries.EXTENSION)
                ? pathStr.substring(0, pathStr.length() - Queries.EXTENSION.length() - 1)
                : path;
        try (var connection = DriverManager.getConnection(Queries.URL_PREFIX + url, Queries.PIXELJ, "")) {
            final var statement = connection.createStatement();
            final var title = extractTitle(statement.executeQuery(Queries.SELECT_TITLE));
            final var metrics = extractMetrics(statement.executeQuery(Queries.SELECT_METRICS));
            final var glyphs = extractGlyphs(statement.executeQuery(Queries.SELECT_GLYPHS), metrics);
            final var kerningPairs = extractKerningPairs(
                    statement.executeQuery(Queries.SELECT_KERNING_PAIRS),
                    glyphs
            );
            return new Project(title, glyphs, kerningPairs, metrics, path);
        } catch (SQLException | IOException e) {
            throw new IOException(
                    String.format("Can't open or read the file %s:\n%s", path.toString(), e.getMessage())
            );
        } catch (NullPointerException | MisshapenDataException | InvalidStateException e) {
            throw new IOException(
                    String.format("%s file is corrupted:\n%s", path.toString(), e.getMessage())
            );
        }

    }

    private static SortedList<KerningPair> extractKerningPairs(ResultSet result, SortedList<Glyph> glyphs)
            throws SQLException,
            IllegalArgumentException {
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

    private static SortedList<Glyph> extractGlyphs(ResultSet result, Metrics metrics)
            throws SQLException,
            IOException,
            MisshapenDataException {
        final var list = new SortedList<Glyph>();
        while (result.next()) {
            final var zipped = new CompressedGlyph(
                    result.getInt(GlyphsColumn.CODE_POINT.getIndex()),
                    result.getInt(GlyphsColumn.WIDTH.getIndex()),
                    result.getBinaryStream(GlyphsColumn.IMAGE_BYTES.getIndex()).readAllBytes()
            );
            list.add(zipped.decompress(metrics.canvasWidth(), metrics.canvasHeight()));
        }
        return list;
    }

    private static Metrics extractMetrics(final ResultSet result) throws InvalidStateException, SQLException {
        result.next();
        return Metrics.ValidatedBuilder.getDefaultBuilder()
                .setCanvasWidth(result.getInt(MetricsColumn.CANVAS_WIDTH.getIndex()))
                .setCanvasHeight(result.getInt(MetricsColumn.CANVAS_HEIGHT.getIndex()))
                .setAscender(result.getInt(MetricsColumn.ASCENDER.getIndex()))
                .setDescender(result.getInt(MetricsColumn.DESCENDER.getIndex()))
                .setCapHeight(result.getInt(MetricsColumn.CAP_HEIGHT.getIndex()))
                .setXHeight(result.getInt(MetricsColumn.X_HEIGHT.getIndex()))
                .setDefaultWidth(result.getInt(MetricsColumn.DEFAULT_WIDTH.getIndex()))
                .setSpacing(result.getInt(MetricsColumn.LETTER_SPACING.getIndex()))
                .setSpaceSize(result.getInt(MetricsColumn.SPACE_SIZE.getIndex()))
                .setLineSpacing(result.getInt(MetricsColumn.LINE_SPACING.getIndex()))
                .setMonospaced(result.getBoolean(MetricsColumn.IS_MONOSPACED.getIndex()))
                .build();
    }

    private static String extractTitle(final ResultSet result) throws SQLException, NullPointerException {
        result.next();
        final var title = result.getString(TitleColumn.TITLE.getIndex());
        if (title == null) {
            throw new NullPointerException("Project title is null");
        }
        return title;
    }

    private ReadService() {
    }
}