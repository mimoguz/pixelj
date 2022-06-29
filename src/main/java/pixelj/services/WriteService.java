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
import pixelj.services.Queries.GlyphsColumn;
import pixelj.services.Queries.KerningPairsColumn;
import pixelj.services.Queries.MetricsColumn;
import pixelj.services.Queries.TitleColumn;

class WriteService {
    public static void save(final Path path, final Project project) throws IOException {
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
                insertGlyph.setInt(GlyphsColumn.CODE_POINT.getIndex(), glyph.codePoint());
                insertGlyph.setInt(GlyphsColumn.WIDTH.getIndex(), glyph.width());
                insertGlyph.setBytes(GlyphsColumn.IMAGE_BYTES.getIndex(), glyph.imageBytes());
                insertGlyph.executeUpdate();
            }

            statement.executeUpdate(Queries.DROP_K_PAIRS_TABLE);
            statement.executeUpdate(Queries.CREATE_KERNING_PAIRS_TABLE);
            final var insertKerningPair = connection.prepareStatement(Queries.INSERT_KERNING_PAIR);
            for (var pair : kerningPairs) {
                insertKerningPair.setInt(KerningPairsColumn.ID.getIndex(), pair.id());
                insertKerningPair.setInt(KerningPairsColumn.LEFT_CODE_POINT.getIndex(), pair.left());
                insertKerningPair.setInt(KerningPairsColumn.RIGHT_CODE_POINT.getIndex(), pair.right());
                insertKerningPair.setInt(KerningPairsColumn.KERNING_VALUE.getIndex(), pair.value());
                insertKerningPair.executeUpdate();
            }

            statement.executeUpdate(Queries.DROP_METRICS_TABLE);
            statement.executeUpdate(Queries.CREATE_METRICS_TABLE);
            final var insertMetrics = connection.prepareStatement(Queries.INSERT_METRICS);
            insertMetrics.setInt(MetricsColumn.CANVAS_WIDTH.getIndex(), metrics.canvasWidth());
            insertMetrics.setInt(MetricsColumn.CANVAS_HEIGHT.getIndex(), metrics.canvasHeight());
            insertMetrics.setInt(MetricsColumn.ASCENDER.getIndex(), metrics.ascender());
            insertMetrics.setInt(MetricsColumn.DESCENDER.getIndex(), metrics.descender());
            insertMetrics.setInt(MetricsColumn.CAP_HEIGHT.getIndex(), metrics.capHeight());
            insertMetrics.setInt(MetricsColumn.X_HEIGHT.getIndex(), metrics.xHeight());
            insertMetrics.setInt(MetricsColumn.DEFAULT_WIDTH.getIndex(), metrics.defaultWidth());
            insertMetrics.setInt(MetricsColumn.LETTER_SPACING.getIndex(), metrics.letterSpacing());
            insertMetrics.setInt(MetricsColumn.SPACE_SIZE.getIndex(), metrics.spaceSize());
            insertMetrics.setInt(MetricsColumn.LINE_SPACING.getIndex(), metrics.lineSpacing());
            insertMetrics.setBoolean(MetricsColumn.IS_MONOSPACED.getIndex(), metrics.isMonospaced());
            insertMetrics.executeUpdate();

            statement.executeUpdate(Queries.DROP_TITLE_TABLE);
            statement.executeUpdate(Queries.CREATE_TITLE_TABLE);
            final var insertTitle = connection.prepareStatement(Queries.INSERT_TITLE);
            insertTitle.setString(TitleColumn.TITLE.getIndex(), title);
            insertTitle.executeUpdate();

            connection.commit();
        } catch (SQLException e) {
            // TODO: Move the message to the resources.
            throw new IOException(String.format("Can't save file %s:\n%s", pathStr, e.getMessage()));
        }
    }

    private WriteService() {
    }
}