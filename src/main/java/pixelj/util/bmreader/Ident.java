package pixelj.util.bmreader;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/** BMFont config file tag and key names. */
public enum Ident implements Token {

    /* -------------------------------- Info tag -------------------------------- */
    INFO("info"),
    FACE("face"),
    SIZE("size"),
    BOLD("bold"),
    ITALIC("italic"),
    CHARSET("charset"),
    UNICODE("unicode"),
    STRETCH_H("stretchH"),
    SMOOTH("smooth"),
    AA("aa"),
    PADDING("padding"),
    SPACING("spacing"),
    OUTLINE("outline"),

    /* ------------------------------- Common tag ------------------------------- */
    COMMON("common"),
    LINE_HEIGHT("lineHeight"),
    BASE("base"),
    SCALE_W("scaleW"),
    SCALE_H("scaleH"),
    PAGES("pages"),
    PACKED("packed"),
    ALPHA_CHANNEL("alphaChnl"),
    RED_CHANNEL("redChnl"),
    GREEN_CHANNEL("greenChnl"),
    BLUE_CHANNEL("blueChnl"),

    /* -------------------------------- Page tag -------------------------------- */
    PAGE("page"),
    ID("id"),
    FILE("file"),

    /* ------------------------------- Chars tag -------------------------------- */
    CHARS("chars"),
    COUNT("count"),

    /* -------------------------------- Char tag -------------------------------- */
    CHAR("char"),
    X("x"),
    Y("y"),
    WIDTH("width"),
    HEIGHT("height"),
    X_OFFSET("xoffset"),
    Y_OFFSET("yoffset"),
    X_ADVANCE("xadvance"),
    CHANNEL("chnl"),
    // Also PAGE, ID

    /* ------------------------------ Kerning tag ------------------------------- */
    KERNING("kerning"),
    FIRST("first"),
    SECOND("second"),
    AMOUNT("amount");

    private static final Map<String, Ident> MAPPING = Arrays.stream(Ident.values())
            .collect(Collectors.toMap(f -> f.getText(), f -> f));

    private static final Set<Ident> TAGS = EnumSet.of(INFO, COMMON, PAGE, CHARS, CHAR, KERNING);

    private final String text;

    Ident(final String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public boolean isTag() {
        return TAGS.contains(this);
    }

    /**
     * Get ident from tag or key name.
     *
     * @param string
     * @return May be null
     */
    public static Ident fromString(final String string) {
        return MAPPING.get(string);
    }
}
