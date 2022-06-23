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

class LoadService {
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
            final var left = glyphs.findHash(result.getInt(2));
            final var right = glyphs.findHash(result.getInt(3));
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
                    result.getInt(1),
                    result.getInt(2),
                    result.getBinaryStream(3).readAllBytes()
            );
            list.add(zipped.decompress(metrics.canvasWidth(), metrics.canvasHeight()));
        }
        return list;
    }

    private static Metrics extractMetrics(final ResultSet result) throws InvalidStateException, SQLException {
        result.next();
        return Metrics.ValidatedBuilder.getDefaultBuilder()
                .setCanvasWidth(result.getInt(1))
                .setCapHeight(result.getInt(2))
                .setAscender(result.getInt(3))
                .setDescender(result.getInt(4))
                .setCapHeight(result.getInt(5))
                .setXHeight(result.getInt(6))
                .setDefaultWidth(result.getInt(7))
                .setSpacing(result.getInt(8))
                .setSpaceSize(result.getInt(9))
                .setLineSpacing(result.getInt(10))
                .setMonospaced(result.getBoolean(11))
                .build();
    }

    private static String extractTitle(final ResultSet result) throws SQLException, NullPointerException {
        result.next();
        final var title = result.getString(1);
        if (title == null) {
            throw new NullPointerException("Project title is null");
        }
        return title;
    }

    private LoadService() {
    }
}