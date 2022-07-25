package pixelj.util.reader;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public enum BmField implements BmToken {

    // Info line
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
    // Common line
    COMMON("common"),
    LINE_HEIGHT("lineHeight"),
    BASE("base"),
    SCALE_W("scaleW"),
    SCALE_H("scaleH"),
    PAGES("pages"),
    PACKED("packed"),
    ALPHA_CHNL("alphaChnl"),
    RED_CHNL("redChnl"),
    GREEN_CHNL("greenChnl"),
    BLUE_CHNL("blueChnl"),
    // Page line
    PAGE("page"),
    ID("id"),
    FILE("file"),
    // Chars line
    CHARS("chars"),
    COUNT("count"),
    // Char line
    CHAR("char"),
    // + ID
    X("x"),
    Y("y"),
    WIDTH("width"),
    HEIGHT("height"),
    XOFFSET("xoffset"),
    YOFFSET("yoffset"),
    XADVANCE("xadvance"),
    // + PAGE
    CHNL("chnl"),
    /// Kerning line
    KERNING("kerning"),
    FIRST("first"),
    SECOND("second"),
    AMOUNT("amount");

    private static final Map<String, BmField> MAPPING = Arrays.stream(BmField.values())
            .collect(Collectors.toMap(f -> f.getText(), f -> f));

    private static final Set<BmField> LINES = EnumSet.of(INFO, COMMON, PAGE, CHARS, CHAR, KERNING);

    private final String text;

    BmField(final String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public boolean isLine() {
        return LINES.contains(this);
    }

    /**
     * @param string
     * @return May be null
     */
    public static BmField fromString(final String string) {
        return MAPPING.get(string);
    }
}
