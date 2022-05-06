package io.github.mimoguz.pixelj.models;

public record Metrics(
        int canvasWidth,
        int canvasHeight,
        int ascender,
        int descender,
        int capHeight,
        int xHeight,
        int defaultCharacterWidth,
        int spacing,
        int spaceSize,
        int lineSpacing,
        boolean isMonospace
) {
}
