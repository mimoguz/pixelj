package pixelj.services;

import java.util.stream.Collectors;
import java.util.stream.Stream;

final class Queries {

    public static final String EXTENSION = "pixj";
    public static final String PIXELJ = "pixelj";
    public static final String URL_PREFIX = "jdbc:h2:" + PIXELJ + ":";
    public static final int TITLE_LENGTH = 100;

    private static final String COMMA = ", ";
    private static final String GLYPHS_TABLE = "glyphs";
    private static final String KERNING_PAIRS_TABLE = "kerning_pairs";
    private static final String SETTINGS_TABLE = "settings";

    public static final String DROP_GLYPHS_TABLE = dropQuery(GLYPHS_TABLE);
    public static final String DROP_K_PAIRS_TABLE = dropQuery(KERNING_PAIRS_TABLE);
    public static final String DROP_SETTINGS_TABLE = dropQuery(SETTINGS_TABLE);

    public static final String CREATE_GLYPHS_TABLE_QUERY = createQuery(
        GLYPHS_TABLE,
        "code_point INT NOT NULL",
        "width INT NOT NULL",
        "image_bytes BINARY VARYING(262144) NOT NULL",
        "PRIMARY KEY (code_point)"
    );

    public static final String CREATE_KERNING_PAIRS_TABLE = createQuery(
        KERNING_PAIRS_TABLE,
        "id BIGINT NOT NULL",
        "left_code_point INT NOT NULL",
        "right_code_point INT NOT NULL",
        "kerning_value INT NOT NULL",
        "PRIMARY KEY (id)"
    );

    public static final String CREATE_SETTINGS_TABLE = createQuery(
            SETTINGS_TABLE,
            "title CHARACTER VARYING(" + TITLE_LENGTH + ") NOT NULL",
            "canvas_width INT NOT NULL",
            "canvas_height INT NOT NULL",
            "ascender INT NOT NULL",
            "descender INT NOT NULL",
            "cap_height INT NOT NULL",
            "x_height INT NOT NULL",
            "default_width INT NOT NULL",
            "letter_spacing INT NOT NULL",
            "space_size INT NOT NULL",
            "line_spacing INT NOT NULL",
            "is_monospaced BOOLEAN NOT NULL",
            "is_bold BOOLEAN NOT NULL",
            "is_italic BOOLEAN NOT NULL"
    );

    public static final String INSERT_SETTINGS = insertQuery(
        SETTINGS_TABLE,
        "title",
        "canvas_width",
        "canvas_height",
        "ascender",
        "descender",
        "cap_height",
        "x_height",
        "default_width",
        "letter_spacing",
        "space_size",
        "line_spacing",
        "is_monospaced",
        "is_bold",
        "is_italic"
    );

    public static final String INSERT_GLYPH = insertQuery(GLYPHS_TABLE, "code_point", "width", "image_bytes");

    public static final String INSERT_KERNING_PAIR = insertQuery(
        KERNING_PAIRS_TABLE,
        "id",
        "left_code_point",
        "right_code_point",
        "kerning_value"
    );

    public static final String SELECT_GLYPHS = selectQuery(GLYPHS_TABLE);
    public static final String SELECT_KERNING_PAIRS = selectQuery(KERNING_PAIRS_TABLE);
    public static final String SELECT_SETTINGS = selectQuery(SETTINGS_TABLE);

    public enum SettingsColumn {
        TITLE,
        CANVAS_WIDTH,
        CANVAS_HEIGHT,
        ASCENDER,
        DESCENDER,
        CAP_HEIGHT,
        X_HEIGHT,
        DEFAULT_WIDTH,
        LETTER_SPACING,
        SPACE_SIZE,
        LINE_SPACING,
        IS_MONOSPACED,
        IS_BOLD,
        IS_ITALIC;

        public int getIndex() {
            return this.ordinal() + 1;
        }
    }

    public enum KerningPairsColumn {
        ID, LEFT_CODE_POINT, RIGHT_CODE_POINT, KERNING_VALUE;

        public int getIndex() {
            return this.ordinal() + 1;
        }
    }

    public enum GlyphsColumn {
        CODE_POINT, WIDTH, IMAGE_BYTES;

        public int getIndex() {
            return this.ordinal() + 1;
        }
    }

    private Queries() {
    }

    private static String createQuery(final String table, final String... columns) {
        return String.format("CREATE TABLE %s (%s);", table, String.join(COMMA, columns));
    }

    private static String dropQuery(final String table) {
        return String.format("DROP TABLE IF EXISTS %s;", table);
    }

    private static String insertQuery(final String table, final String... columns) {
        final var vals = Stream.iterate("?", a -> a).limit(columns.length).collect(Collectors.joining(COMMA));
        return String.format("INSERT INTO %s (%s) VALUES (%s);", table, String.join(COMMA, columns), vals);
    }

    private static String selectQuery(final String table) {
        return String.format("SELECT * FROM %s;", table);
    }
}
