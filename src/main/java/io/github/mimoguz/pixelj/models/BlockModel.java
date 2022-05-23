package io.github.mimoguz.pixelj.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public record BlockModel(
        @JsonProperty("id") int id,
        @JsonProperty("name") String name,
        @JsonProperty("firstCodePoint") int starts,
        @JsonProperty("lastCodePoint") int ends
) {
    @Override
    public String toString() {
        return name;
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof BlockModel other) {
            return id == other.id();
        }
        return false;
    }
}
