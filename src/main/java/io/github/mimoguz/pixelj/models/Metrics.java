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
    public static class Builder {
        private int canvasWidth;
        private int canvasHeight;
        private int ascender;
        private int descender;
        private int capHeight;
        private int xHeight;
        private int defaultCharacterWidth;
        private int spacing;
        private int spaceSize;
        private int lineSpacing;
        private boolean isMonospaced;

        private Builder() {
        }

        public static Builder getDefault() {
            final var builder = new Builder();
            builder.canvasWidth = 24;
            builder.canvasHeight = 24;
            builder.ascender = 15;
            builder.descender = 4;
            builder.capHeight = 11;
            builder.xHeight = 7;
            builder.defaultCharacterWidth = 7;
            builder.spacing = 1;
            builder.spaceSize = 4;
            builder.lineSpacing = 2;
            builder.isMonospaced = false;
            return builder;
        }

        public static Builder from(Metrics metrics) {
            final var builder = new Builder();
            builder.canvasWidth = metrics.canvasWidth();
            builder.canvasHeight = metrics.canvasHeight();
            builder.ascender = metrics.ascender();
            builder.descender = metrics.descender();
            builder.capHeight = metrics.capHeight();
            builder.xHeight = metrics.xHeight();
            builder.defaultCharacterWidth = metrics.defaultCharacterWidth();
            builder.spacing = metrics.spacing();
            builder.spaceSize = metrics.spaceSize();
            builder.lineSpacing = metrics.lineSpacing();
            builder.isMonospaced = metrics.isMonospace();
            return builder;
        }

        public Metrics build() {
            return new Metrics(
                    canvasWidth,
                    canvasHeight,
                    ascender,
                    descender,
                    capHeight,
                    xHeight,
                    defaultCharacterWidth,
                    spacing,
                    spaceSize,
                    lineSpacing,
                    isMonospaced
            );
        }

        public Builder setCanvasWidth(int value) {
            canvasWidth = value;
            return this;
        }

        public Builder setCanvasHeight(int value) {
            canvasHeight = value;
            return this;
        }

        public Builder setAscender(int value) {
            ascender = value;
            return this;
        }

        public Builder setDescender(int value) {
            descender = value;
            return this;
        }

        public Builder setCapHeight(int value) {
            capHeight = value;
            return this;
        }

        public Builder setXHeight(int value) {
            xHeight = value;
            return this;
        }

        public Builder setDefaultCharacterWidth(int value) {
            defaultCharacterWidth = value;
            return this;
        }

        public Builder setSpacing(int value) {
            spacing = value;
            return this;
        }

        public Builder setSpaceSize(int value) {
            spaceSize = value;
            return this;
        }

        public Builder setLineSpacing(int value) {
            lineSpacing = value;
            return this;
        }

        public Builder setMonospaced(boolean value) {
            isMonospaced = value;
            return this;
        }
    }
}
