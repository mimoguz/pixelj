package pixelj.models;

import pixelj.util.ChangeableBoolean;
import pixelj.util.ChangeableInt;
import pixelj.util.ReadOnlyBoolean;
import pixelj.util.ReadOnlyInt;

public record Metrics(
        int canvasWidth,
        int canvasHeight,
        int ascender,
        int descender,
        int capHeight,
        int xHeight,
        int defaultWidth,
        int letterSpacing,
        int spaceSize,
        int lineSpacing,
        boolean isMonospaced
) {

    public static Metrics getDefault() {
        return new Metrics(24, 24, 16, 4, 11, 7, 7, 1, 4, 2, false);
    }

    public static class ValidatedBuilder {
        private static final ChangeableInt ZERO = new ChangeableInt(0);

        public final ChangeableInt ascender = new ChangeableInt(0);
        public final ChangeableInt canvasHeight = new ChangeableInt(0);
        public final ChangeableInt canvasWidth = new ChangeableInt(0);
        public final ChangeableInt capHeight = new ChangeableInt(0);
        public final ChangeableInt defaultWidth = new ChangeableInt(0);
        public final ChangeableInt descender = new ChangeableInt(0);
        public final ChangeableBoolean isMonospaced = new ChangeableBoolean(false);
        public final ChangeableInt lineSpacing = new ChangeableInt(0);
        public final ChangeableInt spaceSize = new ChangeableInt(0);
        public final ChangeableInt letterSpacing = new ChangeableInt(0);

        public final ReadOnlyBoolean validAscender = new ReadOnlyInt(descender)
                .le(canvasHeight.subtract(descender))
                .and(ascender.gt(ZERO));
        public final ReadOnlyBoolean validCanvasHeight = canvasHeight.gt(ZERO);
        public final ReadOnlyBoolean validCanvasWidth = canvasWidth.gt(ZERO);
        public final ReadOnlyBoolean validCapHeight = capHeight.le(ascender).and(capHeight.gt(ZERO));
        public final ReadOnlyBoolean validDefaultWidth = defaultWidth.le(canvasWidth);
        public final ReadOnlyBoolean validDescender = descender.le(canvasHeight).and(descender.gt(ZERO));
        public final ReadOnlyBoolean validLineSpacing = lineSpacing.ge(ZERO);
        public final ReadOnlyBoolean validSpaceSize = spaceSize.ge(ZERO);
        public final ReadOnlyBoolean validLetterSpacing = letterSpacing.ge(ZERO);
        private final ChangeableInt xHeight = new ChangeableInt(0);
        public final ReadOnlyBoolean validXHeight = xHeight.le(capHeight).and(xHeight.gt(ZERO));

        public final ReadOnlyBoolean validAll = validCanvasHeight.and(validCanvasWidth)
                .and(validAscender)
                .and(validDescender)
                .and(validCapHeight)
                .and(validXHeight)
                .and(validDefaultWidth)
                .and(validLetterSpacing)
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
                    defaultWidth.getValue(),
                    letterSpacing.getValue(),
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

        public ValidatedBuilder setDefaultWidth(int value) {
            defaultWidth.setValue(value);
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
            letterSpacing.setValue(value);
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
            builder.defaultWidth.setValue(metrics.defaultWidth);
            builder.descender.setValue(metrics.descender());
            builder.isMonospaced.setValue(metrics.isMonospaced());
            builder.lineSpacing.setValue(metrics.lineSpacing());
            builder.spaceSize.setValue(metrics.spaceSize());
            builder.letterSpacing.setValue(metrics.letterSpacing);
            builder.xHeight.setValue(metrics.xHeight());
            return builder;
        }

        public static ValidatedBuilder getDefaultBuilder() {
            return ValidatedBuilder.from(Metrics.getDefault());
        }

        public static final class InvalidStateException extends Exception {
            public InvalidStateException(final String message) {
                super(message);
            }
        }
    }
}
