package pixelj.util.reader;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public enum LineType {
    INFO("info"),
    COMMON("common"),
    PAGE("page"),
    CHARS("chars"),
    CHAR("char"),
    KERNING("kerning");

    private static final Map<String, LineType> MAPPING = Arrays.stream(LineType.values())
            .collect(Collectors.toMap(f -> f.getText(), f -> f));

    private final String text;

    LineType(final String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    /**
     * @param string
     * @return Line enum with matching text, or null if there isn't any.
     */
    public static LineType fromString(final String string) {
        return MAPPING.get(string);
    }
}
