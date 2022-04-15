package io.github.mimoguz.pixelj.models;

public record Metrics(
        int canvasWidth,
        int canvasHeight,
        int ascender,
        int descender,
        int xHeight,
        int defaultCharacterWidth,
        int spacing,
        boolean isMonospace
) {
}
