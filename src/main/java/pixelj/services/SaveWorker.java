package pixelj.services;

import java.nio.file.Path;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

import javax.swing.SwingWorker;

import pixelj.models.CompressedGlyph;
import pixelj.models.KerningPairRecord;
import pixelj.models.Metrics;

class SaveWorker extends SwingWorker<Boolean, Void> {
    private final String path;
    private final List<CompressedGlyph> glyphs;
    private final List<KerningPairRecord> kerningPairs;
    private final Metrics metrics;
    private final String title;

    public SaveWorker(
            final Path path,
            final List<CompressedGlyph> glyphs,
            final List<KerningPairRecord> kerningPairs,
            final Metrics metrics,
            final String title
    ) {
        this.glyphs = glyphs;
        this.kerningPairs = kerningPairs;
        this.metrics = metrics;
        this.title = title;
        this.path = path.toAbsolutePath().toString();
    }

    @Override
    protected Boolean doInBackground() throws Exception {
        setProgress(0);

        final var url = path.endsWith("." + Queries.EXTENSION)
                ? path.substring(0, path.length() - Queries.EXTENSION.length() - 1)
                : path;
        try (var connection = DriverManager.getConnection(Queries.URL_PREFIX + url, Queries.PIXELJ, "")) {

            connection.setAutoCommit(false);

            final var statement = connection.createStatement();

            statement.executeUpdate(Queries.DROP_GLYPHS_TABLE_QUERY);
            statement.executeUpdate(Queries.CREATE_GLYPHS_TABLE_QUERY);
            final var insertGlyph = connection.prepareStatement(Queries.INSERT_GLYPH_QUERY);
            for (var glyph : glyphs) {
                insertGlyph.setInt(1, glyph.codePoint());
                insertGlyph.setInt(2, glyph.width());
                insertGlyph.setBytes(3, glyph.imageBytes());
                insertGlyph.executeUpdate();
            }

            statement.executeUpdate(Queries.DROP_K_PAIRS_TABLE_QUERY);
            statement.executeUpdate(Queries.CREATE_K_PAIRS_TABLE_QUERY);
            final var insertKerningPair = connection.prepareStatement(Queries.INSERT_K_PAIR_QUERY);
            for (var pair : kerningPairs) {
                insertKerningPair.setInt(1, pair.id());
                insertKerningPair.setInt(2, pair.left());
                insertKerningPair.setInt(3, pair.right());
                insertKerningPair.setInt(4, pair.value());
                insertKerningPair.executeUpdate();
            }

            statement.executeUpdate(Queries.DROP_METRICS_TABLE_QUERY);
            statement.executeUpdate(Queries.CREATE_METRICS_TABLE_QUERY);
            final var insertMetrics = connection.prepareStatement(Queries.INSERT_METRICS_QUERY);
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

            statement.executeUpdate(Queries.DROP_TITLE_TABLE_QUERY);
            statement.executeUpdate(Queries.CREATE_TITLE_TABLE_QUERY);
            final var insertTitle = connection.prepareStatement(Queries.INSERT_TITLE_QUERY);
            insertTitle.setString(1, title);
            insertTitle.executeUpdate();

            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            setProgress(-1);
            return false;
        }

        setProgress(100);
        return true;
    }
}