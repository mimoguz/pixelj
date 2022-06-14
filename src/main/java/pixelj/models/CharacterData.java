package pixelj.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CharacterData(
        @JsonProperty("codePoint") int codePoint,
        @JsonProperty("name") String name,
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
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof CharacterData other) {
            return codePoint == other.codePoint();
        }
        return false;
    }
}