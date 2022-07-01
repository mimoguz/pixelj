package pixelj.services;

class Queries {
    private static final String GLYPHS_TABLE = "glyphs";
    private static final String KERNING_PAIRS_TABLE = "kerning_pairs";
    private static final String SETTINGS_TABLE = "settings";
    private static final String PROJECT_TABLE = "project";

    public static final String EXTENSION = "pixj";
    public static final String PIXELJ = "pixelj";
    public static final String URL_PREFIX = "jdbc:h2:" + PIXELJ + ":";

    public static final String DROP_GLYPHS_TABLE = "DROP TABLE IF EXISTS " + GLYPHS_TABLE + ";";
    public static final String DROP_K_PAIRS_TABLE = "DROP TABLE IF EXISTS " + KERNING_PAIRS_TABLE + ";";
    public static final String DROP_SETTINGS_TABLE = "DROP TABLE IF EXISTS " + SETTINGS_TABLE + ";";
    public static final String DROP_PROJECT_TABLE = "DROP TABLE IF EXISTS " + PROJECT_TABLE + ";";

    public static final String CREATE_GLYPHS_TABLE_QUERY = "CREATE TABLE " + GLYPHS_TABLE + "(" + """
                code_point INT NOT NULL,
                width INT NOT NULL,
                image_bytes BINARY VARYING(262144) NOT NULL,
                PRIMARY KEY (code_point)
            );
            """;

    public static final String CREATE_KERNING_PAIRS_TABLE = "CREATE TABLE " + KERNING_PAIRS_TABLE + "(" + """
                id INT NOT NULL,
                left_code_point INT NOT NULL,
                right_code_point INT NOT NULL,
                kerning_value INT NOT NULL,
                PRIMARY KEY (id)
            );
            """;

    public static final String CREATE_SETTINGS_TABLE = "CREATE TABLE " + SETTINGS_TABLE + "(" + """
                canvas_width INT NOT NULL,
                canvas_height INT NOT NULL,
                ascender INT NOT NULL,
                descender INT NOT NULL,
                cap_height INT NOT NULL,
                x_height INT NOT NULL,
                default_width INT NOT NULL,
                letter_spacing INT NOT NULL,
                space_size INT NOT NULL,
                line_spacing INT NOT NULL,
                is_monospaced BOOLEAN NOT NULL,
                is_bold BOOLEAN NOT NULL,
                is_italic BOOLEAN NOT NULL
            );
            """;

    public static final String CREATE_PROJECT_TABLE = "CREATE TABLE " + PROJECT_TABLE
            + "(title CHARACTER VARYING(100) NOT NULL, save_version INT NOT NULL);";

    public static final String INSERT_SETTINGS = "INSERT INTO " + SETTINGS_TABLE + " " + """
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
                is_monospaced,
                is_bold,
                is_italic
            )
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);
            """;

    public static final String INSERT_GLYPH = "INSERT INTO " + GLYPHS_TABLE
            + " (code_point, width, image_bytes) VALUES (?, ?, ?);";

    public static final String INSERT_PROJECT = "INSERT INTO " + PROJECT_TABLE
            + " (title, save_version) VALUES (?, ?);";

    public static final String INSERT_KERNING_PAIR = "INSERT INTO " + KERNING_PAIRS_TABLE
            + " (id, left_code_point, right_code_point, kerning_value) VALUES (?, ?, ?, ?);";

    public static final String SELECT_GLYPHS = "SELECT * FROM " + GLYPHS_TABLE + ";";
    public static final String SELECT_KERNING_PAIRS = "SELECT * FROM " + KERNING_PAIRS_TABLE + ";";
    public static final String SELECT_SETTINGS = "SELECT * FROM " + SETTINGS_TABLE + ";";
    public static final String SELECT_TITLE = "SELECT * FROM " + PROJECT_TABLE + ";";

    public enum SettingsColumn {
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

    public enum TitleColumn {
        TITLE, SAVE_VERSION;

        public int getIndex() {
            return this.ordinal() + 1;
        }
    }

    private Queries() {
    }
}
