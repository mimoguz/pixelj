package io.github.mimoguz.pixelj.models;

import io.github.mimoguz.pixelj.util.ChangeableBoolean;
import io.github.mimoguz.pixelj.util.ChangeableInt;
import io.github.mimoguz.pixelj.util.ReadOnlyBoolean;
import io.github.mimoguz.pixelj.util.ReadOnlyInt;

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
        boolean isMonospaced
) {
    public static Metrics getDefault() {
        return new Metrics(
                24,
                24,
                16,
                4,
                11,
                7,
                7,
                1,
                4,
                2,
                false
        );
    }

    public static class ValidatedBuilder {
        private static final ChangeableInt ZERO = new ChangeableInt(0);

        public final ChangeableInt ascender = new ChangeableInt(0);
        public final ChangeableInt canvasHeight = new ChangeableInt(0);
        public final ChangeableInt canvasWidth = new ChangeableInt(0);
        public final ChangeableInt capHeight = new ChangeableInt(0);
        public final ChangeableInt defaultCharacterWidth = new ChangeableInt(0);
        public final ChangeableInt descender = new ChangeableInt(0);
        public final ChangeableBoolean isMonospaced = new ChangeableBoolean(false);
        public final ChangeableInt lineSpacing = new ChangeableInt(0);
        public final ChangeableInt spaceSize = new ChangeableInt(0);
        public final ChangeableInt spacing = new ChangeableInt(0);
        public final ReadOnlyBoolean validAscender = new ReadOnlyInt(descender)
                .le(canvasHeight.subtract(ascender))
                .and(ascender.gt(ZERO));
        public final ReadOnlyBoolean validCanvasHeight = canvasHeight.gt(ZERO);
        public final ReadOnlyBoolean validCanvasWidth = canvasWidth.gt(ZERO);
        public final ReadOnlyBoolean validCapHeight = capHeight.le(ascender).and(capHeight.gt(ZERO));
        public final ReadOnlyBoolean validDefaultCharacterWidth = defaultCharacterWidth.le(canvasWidth);
        public final ReadOnlyBoolean validDescender = descender.le(canvasHeight).and(descender.gt(ZERO));
        public final ReadOnlyBoolean validLineSpacing = lineSpacing.ge(ZERO);
        public final ReadOnlyBoolean validSpaceSize = spaceSize.ge(ZERO);
        public final ReadOnlyBoolean validSpacing = spacing.ge(ZERO);
        private final ChangeableInt xHeight = new ChangeableInt(0);
        public final ReadOnlyBoolean validXHeight = xHeight.le(capHeight).and(xHeight.gt(ZERO));

        public final ReadOnlyBoolean validAll = validCanvasHeight
                .and(validCanvasWidth)
                .and(validAscender)
                .and(validDescender)
                .and(validCapHeight)
                .and(validXHeight)
                .and(validDefaultCharacterWidth)
                .and(validSpacing)
                .and(validLineSpacing)
                .and(validSpaceSize);

        public ValidatedBuilder() {
        }

        public Metrics build() throws InvalidStateException {
            if (!validAll.getValue()) {
                throw new InvalidStateException("Not all fields are valid");
            }
            return new Metrics(
                    canvasWidth.getValue(),
                    canvasHeight.getValue(),
                    ascender.getValue(),
                    descender.getValue(),
                    capHeight.getValue(),
                    xHeight.getValue(),
                    defaultCharacterWidth.getValue(),
                    spacing.getValue(),
                    spaceSize.getValue(),
                    lineSpacing.getValue(),
                    isMonospaced.getValue()
            );
        }

        public ValidatedBuilder setAscender(int value) {
            ascender.setValue(value);
            return this;
        }

        public ValidatedBuilder setCanvasHeight(int value) {
            canvasHeight.setValue(value);
            return this;
        }

        public ValidatedBuilder setCanvasWidth(int value) {
            canvasWidth.setValue(value);
            return this;
        }

        public ValidatedBuilder setCapHeight(int value) {
            capHeight.setValue(value);
            return this;
        }

        public ValidatedBuilder setDefaultCharacterWidth(int value) {
            defaultCharacterWidth.setValue(value);
            return this;
        }

        public ValidatedBuilder setDescender(int value) {
            descender.setValue(value);
            return this;
        }

        public ValidatedBuilder setLineSpacing(int value) {
            lineSpacing.setValue(value);
            return this;
        }

        public ValidatedBuilder setMonospaced(boolean value) {
            isMonospaced.setValue(value);
            return this;
        }

        public ValidatedBuilder setSpaceSize(int value) {
            spaceSize.setValue(value);
            return this;
        }

        public ValidatedBuilder setSpacing(int value) {
            spacing.setValue(value);
            return this;
        }

        public ValidatedBuilder setXHeight(int value) {
            xHeight.setValue(value);
            return this;
        }

        public static ValidatedBuilder from(Metrics metrics) {
            final var builder = new ValidatedBuilder();
            builder.ascender.setValue(metrics.ascender());
            builder.canvasHeight.setValue(metrics.canvasHeight());
            builder.canvasWidth.setValue(metrics.canvasWidth());
            builder.capHeight.setValue(metrics.capHeight());
            builder.defaultCharacterWidth.setValue(metrics.defaultCharacterWidth());
            builder.descender.setValue(metrics.descender());
            builder.isMonospaced.setValue(metrics.isMonospaced());
            builder.lineSpacing.setValue(metrics.lineSpacing());
            builder.spaceSize.setValue(metrics.spaceSize());
            builder.spacing.setValue(metrics.spacing());
            builder.xHeight.setValue(metrics.xHeight());
            return builder;
        }

        public static ValidatedBuilder getDefaultBuilder() {
            return ValidatedBuilder.from(Metrics.getDefault());
        }

        public static final class InvalidStateException extends Exception {
            private final String message;

            public InvalidStateException(final String message) {
                this.message = message;
            }

            @Override
            public String getMessage() {
                return message;
            }
        }
    }
}
