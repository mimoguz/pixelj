package pixelj.services;

import java.nio.file.Path;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

import org.h2.store.fs.FilePath;

import pixelj.models.CompressedGlyph;
import pixelj.models.KerningPair;
import pixelj.models.Metrics;
import pixelj.models.Project;

public class DBService {
    private static final String PIXELJ = "pixelj";
    private static final String URL_PREFIX = "jdbc:h2:" + PIXELJ + ":";
    private static final String GLYPHS_TABLE = "glyphs";
    private static final String KERNING_PAIRS_TABLE = "kerning_pairs";
    private static final String METRICS_TABLE = "metrics";
    private static final String TITLE_TABLE = "title";
    private static final String TMP = "_tmp";

    private static final String DROP_TABLE_QUERY = "DROP TABLE IF EXISTS ?";

    private static final String CREATE_GLYPHS_TABLE_QUERY = """
            CREATE TABLE glyphs_tmp(
                code_point INT NOT NULL,
                width INT NOT NULL,
                image_bytes BINARY VARYING(262144) NOT NULL,
                PRIMARY KEY (code_point)
            );
            """;

    private static final String CREATE_KERNING_PAIRS_TABLE_QUERY = """
            CREATE TABLE glyphs_tmp(
                left INT NOT NULL,
                right INT NOT NULL,
                value INT NOT NULL,
                CONSTRAINT pk PRIMARY KEY (left, right)
            );
            """;

    private static final String CREATE_METRICS_TABLE_QUERY = """
            CREATE TABLE metrics_tmp(
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

    private static final String CREATE_TITLE_TABLE_QUERY = """
            CREATE TABLE title_tmp(
                title CHARACTER VARYING(100) NOT NULL
            );
            """;

    private static final String RENAME_TABLE_QUERY = "RENAME ? TO ?;";

    static {
        final var wrapper = new CustomExtensionWrapper(PIXELJ, PIXELJ);
        FilePath.register(wrapper);
    }

    public static boolean writeFile(Project project, Path path) {
        // TODO: Saving logic
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

        try (
                var connection = DriverManager
                        .getConnection(URL_PREFIX + path.toAbsolutePath().toString(), PIXELJ, "")
        ) {
            final var statement = connection.createStatement();
            statement.executeQuery(CREATE_GLYPHS_TABLE_QUERY);
        } catch (SQLException exception) {
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
}
