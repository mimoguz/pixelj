package pixelj.util.bmreader2;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

/** Words of a BmFont config file. */
public enum Word {

    AA("aa"),
    ALPHA_CHANNEL("alphaChnl"),
    AMOUNT("amount"),
    BASE("base"),
    BLUE_CHANNEL("blueChnl"),
    BOLD("bold"),
    CHANNEL("chnl"),
    CHAR("char"),
    CHARS("chars"),
    CHARSET("charset"),
    COMMON("common"),
    COUNT("count"),
    FACE("face"),
    FILE("file"),
    FIRST("first"),
    GREEN_CHANNEL("greenChnl"),
    HEIGHT("height"),
    ID("id"),
    INFO("info"),
    ITALIC("italic"),
    KERNING("kerning"),
    LINE_HEIGHT("lineHeight"),
    OUTLINE("outline"),
    PACKED("packed"),
    PADDING("padding"),
    PAGE("page"),
    PAGES("pages"),
    RED_CHANNEL("redChnl"),
    SCALE_H("scaleH"),
    SCALE_W("scaleW"),
    SECOND("second"),
    SIZE("size"),
    SMOOTH("smooth"),
    SPACING("spacing"),
    STRETCH_H("stretchH"),
    UNICODE("unicode"),
    WIDTH("width"),
    X_ADVANCE("xadvance"),
    X_OFFSET("xoffset"),
    X("x"),
    Y_OFFSET("yoffset"),
    Y("y");

    private static final Map<String, Word> MAPPING = Arrays.stream(values())
        .collect(Collectors.toMap(f -> f.getText(), f -> f));

    private final String text;

    Word(final String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    /**
     * @param string
     * @return May be null
     */
    public static Word fromString(final String string) {
        return MAPPING.get(string);
    }
}
