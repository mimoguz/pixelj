package pixelj.models;

import pixelj.util.ChangeableBoolean;
import pixelj.util.ChangeableInt;
import pixelj.util.ReadOnlyBoolean;
import pixelj.util.ReadOnlyInt;

public record DocumentSettings(
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
        boolean isMonospaced,
        boolean isBold,
        boolean isItalic
) {

    public static DocumentSettings getDefault() {
        return new DocumentSettings(24, 24, 16, 4, 11, 7, 7, 1, 4, 2, false, false, false);
    }

    public static class Builder {
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
        public final ChangeableBoolean isBold = new ChangeableBoolean(false);
        public final ChangeableBoolean isItalic = new ChangeableBoolean(false);

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

        public Builder() {
        }

        public DocumentSettings build() throws InvalidStateException {
            if (!validAll.getValue()) {
                throw new InvalidStateException("Not all fields are valid");
            }
            return new DocumentSettings(
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
                    isMonospaced.getValue(),
                    isBold.getValue(),
                    isItalic.getValue()
            );
        }

        public Builder setAscender(int value) {
            ascender.setValue(value);
            return this;
        }

        public Builder setBold(boolean value) {
            isBold.setValue(value);
            return this;
        }

        public Builder setCanvasHeight(int value) {
            canvasHeight.setValue(value);
            return this;
        }

        public Builder setCanvasWidth(int value) {
            canvasWidth.setValue(value);
            return this;
        }

        public Builder setCapHeight(int value) {
            capHeight.setValue(value);
            return this;
        }

        public Builder setDefaultWidth(int value) {
            defaultWidth.setValue(value);
            return this;
        }

        public Builder setDescender(int value) {
            descender.setValue(value);
            return this;
        }

        public Builder setItalic(boolean value) {
            isItalic.setValue(value);
            return this;
        }

        public Builder setLineSpacing(int value) {
            lineSpacing.setValue(value);
            return this;
        }

        public Builder setMonospaced(boolean value) {
            isMonospaced.setValue(value);
            return this;
        }

        public Builder setSpaceSize(int value) {
            spaceSize.setValue(value);
            return this;
        }

        public Builder setLetterSpacing(int value) {
            letterSpacing.setValue(value);
            return this;
        }

        public Builder setXHeight(int value) {
            xHeight.setValue(value);
            return this;
        }

        public static Builder from(DocumentSettings settings) {
            return new Builder().setAscender(settings.ascender())
                    .setBold(settings.isBold())
                    .setCanvasHeight(settings.canvasHeight())
                    .setCanvasWidth(settings.canvasWidth())
                    .setCapHeight(settings.capHeight())
                    .setDefaultWidth(settings.defaultWidth)
                    .setDescender(settings.descender())
                    .setItalic(settings.isItalic)
                    .setLineSpacing(settings.lineSpacing())
                    .setMonospaced(settings.isMonospaced())
                    .setSpaceSize(settings.spaceSize())
                    .setLetterSpacing(settings.letterSpacing)
                    .setXHeight(settings.xHeight());
        }

        public static Builder getDefaultBuilder() {
            return Builder.from(DocumentSettings.getDefault());
        }

        public static final class InvalidStateException extends Exception {
            public InvalidStateException(final String message) {
                super(message);
            }
        }
    }
}
