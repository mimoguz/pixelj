package pixelj.services;

import java.nio.file.Path;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.swing.SwingWorker;

import org.h2.store.fs.FilePath;

import pixelj.models.CompressedGlyph;
import pixelj.models.KerningPair;
import pixelj.models.Metrics;
import pixelj.models.Project;

public class DBService {
    private static final String PIXELJ = "pixelj";
    private static final String URL_PREFIX = "jdbc:h2:" + PIXELJ + ":";
    private static final String GLYPHS_TABLE = "glyphs";
    private static final String K_PAIRS_TABLE = "kerning_pairs";
    private static final String METRICS_TABLE = "metrics";
    private static final String TITLE_TABLE = "title";

    private static final String DROP_GLYPHS_TABLE_QUERY = "DROP TABLE IF EXISTS " + GLYPHS_TABLE + ";";
    private static final String DROP_K_PAIRS_TABLE_QUERY = "DROP TABLE IF EXISTS " + K_PAIRS_TABLE + ";";
    private static final String DROP_METRICS_TABLE_QUERY = "DROP TABLE IF EXISTS " + METRICS_TABLE + ";";
    private static final String DROP_TITLE_TABLE_QUERY = "DROP TABLE IF EXISTS " + TITLE_TABLE + ";";

    private static final String CREATE_GLYPHS_TABLE_QUERY = "CREATE TABLE " + GLYPHS_TABLE + "(" + """
                code_point INT NOT NULL,
                width INT NOT NULL,
                image_bytes BINARY VARYING(262144) NOT NULL,
                PRIMARY KEY (code_point)
            );
            """;

    private static final String CREATE_K_PAIRS_TABLE_QUERY = "CREATE TABLE " + K_PAIRS_TABLE + "(" + """
                left INT NOT NULL,
                right INT NOT NULL,
                value INT NOT NULL,
                CONSTRAINT pk PRIMARY KEY (left, right)
            );
            """;

    private static final String CREATE_METRICS_TABLE_QUERY = "CREATE TABLE " + METRICS_TABLE + "(" + """
                canvas_width INT NOT NULL ,
                canvas_height INT NOT NULL ,
                ascender INT NOT NULL ,
                descender INT NOT NULL ,
                cap_height INT NOT NULL ,
                x_height INT NOT NULL ,
                default_width INT NOT NULL ,
                letter_spacing INT NOT NULL ,
                space_size INT NOT NULL ,
                line_spacing INT NOT NULL ,
                is_monospaced BOOLEAN NOT NULL
            );
            """;

    private static final String CREATE_TITLE_TABLE_QUERY = "CREATE TABLE " + TITLE_TABLE
            + "(title CHARACTER VARYING(100) NOT NULL);";

    private static final String INSERT_METRICS_QUERY = "INSERT INTO " + METRICS_TABLE + " " + """
            (
                canvas_width,
                canvas_height,
                ascender,
                descender,
                cap_height,
                x_height,
                default_width,
                letter_spacing,
                space_size,
                line_spacing,
                is_monospaced
            )
            VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? );
            """;

    private static final String INSERT_GLYPH_QUERY = "INSERT INTO " + GLYPHS_TABLE
            + " (code_point, width, image_bytes) VALUES (?, ?, ?);";

    private static final String INSERT_TITLE_QUERY = "INSERT INTO " + TITLE_TABLE + " (title) VALUES (?);";

    private static final String INSERT_K_PAIR_QUERY = "INSERT INTO " + K_PAIRS_TABLE
            + " (left, right, value) VALUES (?, ?, ?);";

    static {
        final var wrapper = new CustomExtensionWrapper(PIXELJ, PIXELJ);
        FilePath.register(wrapper);
    }

    public static boolean writeFile(Project project, Path path) {
        List<CompressedGlyph> glyphs;
        List<KerningPairRecord> kerningPairs;
        String title;
        final var metrics = project.getMetrics();

        synchronized (project) {
            title = project.getTitle();
            glyphs = project.getGlyphs().getElements().parallelStream().map(CompressedGlyph::from).toList();
            kerningPairs = project.getKerningPairs()
                    .getElements()
                    .stream()
                    .map(KerningPairRecord::from)
                    .toList();
        }

        try {
            (new SaveWorker(path, glyphs, kerningPairs, metrics, title)).doInBackground();
        } catch (Exception e) {
            return false;
        }

        return true;
    }

    public static Project loadFile(Path path) {
        return null;
    }

    private record KerningPairRecord(int left, int right, int value) {
        public static KerningPairRecord from(KerningPair p) {
            return new KerningPairRecord(
                    p.getLeft().getCodePoint(),
                    p.getRight().getCodePoint(),
                    p.getKerningValue()
            );
        }
    }

    private static class SaveWorker extends SwingWorker<Boolean, Void> {
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
        protected Boolean doInBackground() {
            try (var connection = DriverManager.getConnection(URL_PREFIX + path, PIXELJ, "")) {
                connection.setAutoCommit(false);

                final var statement = connection.createStatement();

                statement.executeQuery(DROP_GLYPHS_TABLE_QUERY);
                statement.executeQuery(CREATE_GLYPHS_TABLE_QUERY);
                final var insertGlyph = connection.prepareStatement(INSERT_GLYPH_QUERY);
                for (var glyph : glyphs) {
                    insertGlyph.setInt(1, glyph.codePoint());
                    insertGlyph.setInt(2, glyph.width());
                    insertGlyph.setBytes(3, glyph.imageBytes());
                    insertGlyph.executeUpdate();
                }

                statement.executeQuery(DROP_K_PAIRS_TABLE_QUERY);
                statement.executeQuery(CREATE_K_PAIRS_TABLE_QUERY);
                final var insertKerningPair = connection.prepareStatement(INSERT_K_PAIR_QUERY);
                for (var pair : kerningPairs) {
                    insertKerningPair.setInt(1, pair.left());
                    insertKerningPair.setInt(2, pair.right());
                    insertKerningPair.setInt(3, pair.value());
                    insertKerningPair.executeUpdate();
                }

                statement.executeQuery(DROP_METRICS_TABLE_QUERY);
                statement.executeQuery(CREATE_METRICS_TABLE_QUERY);
                final var insertMetrics = connection.prepareStatement(INSERT_METRICS_QUERY);
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

                statement.executeQuery(DROP_TITLE_TABLE_QUERY);
                statement.executeQuery(CREATE_TITLE_TABLE_QUERY);
                final var insertTitle = connection.prepareStatement(INSERT_TITLE_QUERY);
                insertTitle.setString(1, title);
                insertTitle.executeUpdate();

                connection.commit();
            } catch (SQLException e) {
                return false;
            }

            return true;
        }

        @Override
        public void done() {
            try {
                final var result = get();
                System.out.println("Save result: " + result);
                // TODO: Inform user
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
                // TODO: Inform user
            }
        }
    }
}
