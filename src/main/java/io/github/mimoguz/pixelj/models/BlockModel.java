package io.github.mimoguz.pixelj.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public record BlockModel(
        @JsonProperty("id") int id,
        @JsonProperty("name") String name,
        @JsonProperty("firstCodePoint") int starts,
        @JsonProperty("lastCodePoint") int ends
) {
}
