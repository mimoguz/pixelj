package pixelj.services;

import java.nio.file.Path;
import java.util.List;

import org.h2.store.fs.FilePath;

import pixelj.models.CompressedGlyph;
import pixelj.models.KerningPairRecord;
import pixelj.models.Project;

public class FileService {
    public static final String EXTENSION = "pixj";
    public static final String PIXELJ = "pixelj";
    // private static final String URL_PREFIX = "jdbc:h2:" + PIXELJ + ":";
    static final String URL_PREFIX = "jdbc:h2:" + PIXELJ + ":";
    private static final String GLYPHS_TABLE = "glyphs";
    private static final String K_PAIRS_TABLE = "kerning_pairs";
    private static final String METRICS_TABLE = "metrics";
    private static final String TITLE_TABLE = "title";

    static final String DROP_GLYPHS_TABLE_QUERY = "DROP TABLE IF EXISTS " + GLYPHS_TABLE + ";";
    static final String DROP_K_PAIRS_TABLE_QUERY = "DROP TABLE IF EXISTS " + K_PAIRS_TABLE + ";";
    static final String DROP_METRICS_TABLE_QUERY = "DROP TABLE IF EXISTS " + METRICS_TABLE + ";";
    static final String DROP_TITLE_TABLE_QUERY = "DROP TABLE IF EXISTS " + TITLE_TABLE + ";";

    static final String CREATE_GLYPHS_TABLE_QUERY = "CREATE TABLE " + GLYPHS_TABLE + "(" + """
                code_point INT NOT NULL,
                width INT NOT NULL,
                image_bytes BINARY VARYING(262144) NOT NULL,
                PRIMARY KEY (code_point)
            );
            """;

    static final String CREATE_K_PAIRS_TABLE_QUERY = "CREATE TABLE " + K_PAIRS_TABLE + "(" + """
                id INT NOT NULL,
                left_code_point INT NOT NULL,
                right_code_point INT NOT NULL,
                kerning_value INT NOT NULL,
                PRIMARY KEY (id)
            );
            """;

    static final String CREATE_METRICS_TABLE_QUERY = "CREATE TABLE " + METRICS_TABLE + "(" + """
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

    static final String CREATE_TITLE_TABLE_QUERY = "CREATE TABLE " + TITLE_TABLE
            + "(title CHARACTER VARYING(100) NOT NULL);";

    static final String INSERT_METRICS_QUERY = "INSERT INTO " + METRICS_TABLE + " " + """
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

    static final String INSERT_GLYPH_QUERY = "INSERT INTO " + GLYPHS_TABLE
            + " (code_point, width, image_bytes) VALUES (?, ?, ?);";

    static final String INSERT_TITLE_QUERY = "INSERT INTO " + TITLE_TABLE + " (title) VALUES (?);";

    static final String INSERT_K_PAIR_QUERY = "INSERT INTO " + K_PAIRS_TABLE
            + " (id, left_code_point, right_code_point, kerning_value) VALUES (?, ?, ?, ?);";

    static {
        final var wrapper = new CustomExtensionWrapper();
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
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public static Project loadFile(Path path) {
        return null;
    }
}
