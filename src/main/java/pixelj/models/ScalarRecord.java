package pixelj.models;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * A unicode scalar.
 *
 * @param codePoint
 * @param name
 * @param generalCategory https://www.unicode.org/reports/tr44/#General_Category_Values
 * @param blockId
 */
public record ScalarRecord(
        @JsonProperty("codePoint") int codePoint,
        @JsonProperty("name") String name,
        @JsonProperty("generalCategory") GeneralCategory generalCategory,
        @JsonProperty("blockId") int blockId
) {

    @Override
    public String toString() {
        return name;
    }

    @Override
    public int hashCode() {
        return codePoint;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof ScalarRecord other) {
            return codePoint == other.codePoint();
        }
        return false;
    }
}
