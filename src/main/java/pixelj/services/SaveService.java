package pixelj.services;

import java.io.IOException;
import java.nio.file.Path;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

import pixelj.models.CompressedGlyph;
import pixelj.models.KerningPairRecord;
import pixelj.models.Metrics;
import pixelj.models.Project;

class SaveService {

    // TODO: Use enums for field indices
    public static Boolean save(final Path path, final Project project) throws IOException {
        List<CompressedGlyph> glyphs;
        List<KerningPairRecord> kerningPairs;
        String title;
        Metrics metrics;

        synchronized (project) {
            title = project.getTitle();
            metrics = project.getMetrics();
            glyphs = project.getGlyphs().getElements().parallelStream().map(CompressedGlyph::from).toList();
            kerningPairs = project.getKerningPairs()
                    .getElements()
                    .stream()
                    .map(KerningPairRecord::from)
                    .toList();
        }

        final var pathStr = path.toAbsolutePath().toString();
        final var url = pathStr.endsWith("." + Queries.EXTENSION)
                ? pathStr.substring(0, pathStr.length() - Queries.EXTENSION.length() - 1)
                : pathStr;

        try (var connection = DriverManager.getConnection(Queries.URL_PREFIX + url, Queries.PIXELJ, "")) {

            connection.setAutoCommit(false);

            final var statement = connection.createStatement();

            statement.executeUpdate(Queries.DROP_GLYPHS_TABLE);
            statement.executeUpdate(Queries.CREATE_GLYPHS_TABLE_QUERY);
            final var insertGlyph = connection.prepareStatement(Queries.INSERT_GLYPH);
            for (var glyph : glyphs) {
                insertGlyph.setInt(1, glyph.codePoint());
                insertGlyph.setInt(2, glyph.width());
                insertGlyph.setBytes(3, glyph.imageBytes());
                insertGlyph.executeUpdate();
            }

            statement.executeUpdate(Queries.DROP_K_PAIRS_TABLE);
            statement.executeUpdate(Queries.CREATE_KERNING_PAIRS_TABLE);
            final var insertKerningPair = connection.prepareStatement(Queries.INSERT_KERNING_PAIR);
            for (var pair : kerningPairs) {
                insertKerningPair.setInt(1, pair.id());
                insertKerningPair.setInt(2, pair.left());
                insertKerningPair.setInt(3, pair.right());
                insertKerningPair.setInt(4, pair.value());
                insertKerningPair.executeUpdate();
            }

            statement.executeUpdate(Queries.DROP_METRICS_TABLE);
            statement.executeUpdate(Queries.CREATE_METRICS_TABLE);
            final var insertMetrics = connection.prepareStatement(Queries.INSERT_METRICS);
            insertMetrics.setInt(1, metrics.canvasWidth());
            insertMetrics.setInt(2, metrics.canvasHeight());
            insertMetrics.setInt(3, metrics.ascender());
            insertMetrics.setInt(4, metrics.descender());
            insertMetrics.setInt(5, metrics.capHeight());
            insertMetrics.setInt(6, metrics.xHeight());
            insertMetrics.setInt(7, metrics.defaultWidth());
            insertMetrics.setInt(8, metrics.letterSpacing());
            insertMetrics.setInt(9, metrics.spaceSize());
            insertMetrics.setInt(10, metrics.lineSpacing());
            insertMetrics.setBoolean(11, metrics.isMonospaced());
            insertMetrics.executeUpdate();

            statement.executeUpdate(Queries.DROP_TITLE_TABLE);
            statement.executeUpdate(Queries.CREATE_TITLE_TABLE);
            final var insertTitle = connection.prepareStatement(Queries.INSERT_TITLE);
            insertTitle.setString(1, title);
            insertTitle.executeUpdate();

            connection.commit();
            return true;
        } catch (SQLException e) {
            // TODO: Move the message to the resources.
            throw new IOException(String.format("Can't save file %s:\n%s", pathStr, e.getMessage()));
        }
    }

    private SaveService() {
    }
}