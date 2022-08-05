package pixelj.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Block(
    @JsonProperty("id") int id,
    @JsonProperty("name") String name,
    @JsonProperty("firstCodePoint") int starts,
    @JsonProperty("lastCodePoint") int ends
) implements HasId {

    @Override
    public String toString() {
        return name;
    }

    @Override
    public long getId() {
        // TODO Auto-generated method stub
        return id;
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof Block other) {
            return id == other.id();
        }
        return false;
    }
}
