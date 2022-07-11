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

    /**
     * @return Default settings
     */
    public static DocumentSettings getDefault() {
        return new DocumentSettings(24, 24, 16, 4, 11, 7, 7, 1, 4, 2, false, false, false);
    }

    public static final class Builder {
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

        /**
         * @return Document settings
         * @throws InvalidStateException
         */
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

        /**
         * @param value Ascender
         * @return This
         */
        public Builder setAscender(final int value) {
            ascender.setValue(value);
            return this;
        }

        /**
         * @param value Is bold
         * @return This
         */
        public Builder setBold(final boolean value) {
            isBold.setValue(value);
            return this;
        }

        /**
         * @param value Canvas height
         * @return This
         */
        public Builder setCanvasHeight(final int value) {
            canvasHeight.setValue(value);
            return this;
        }

        /**
         * @param value Canvas width
         * @return This
         */
        public Builder setCanvasWidth(final int value) {
            canvasWidth.setValue(value);
            return this;
        }

        /**
         * @param value Cap height
         * @return This
         */
        public Builder setCapHeight(final int value) {
            capHeight.setValue(value);
            return this;
        }

        /**
         * @param value Default glyph width
         * @return This
         */
        public Builder setDefaultWidth(final int value) {
            defaultWidth.setValue(value);
            return this;
        }

        /**
         * @param value Descender
         * @return This
         */
        public Builder setDescender(final int value) {
            descender.setValue(value);
            return this;
        }

        /**
         * @param value Is italic
         * @return This
         */
        public Builder setItalic(final boolean value) {
            isItalic.setValue(value);
            return this;
        }

        /**
         * @param value Extra line spacing
         * @return This
         */
        public Builder setLineSpacing(final int value) {
            lineSpacing.setValue(value);
            return this;
        }

        /**
         * @param value Is monospaced
         * @return This
         */
        public Builder setMonospaced(final boolean value) {
            isMonospaced.setValue(value);
            return this;
        }

        /**
         * @param value Space width
         * @return This
         */
        public Builder setSpaceSize(final int value) {
            spaceSize.setValue(value);
            return this;
        }

        /**
         * @param value Letter spacing
         * @return This
         */
        public Builder setLetterSpacing(final int value) {
            letterSpacing.setValue(value);
            return this;
        }

        /**
         * @param value x Height
         * @return This
         */
        public Builder setXHeight(final int value) {
            xHeight.setValue(value);
            return this;
        }

        /**
         * @param settings Source
         * @return A builder using the values from the source
         */
        public static Builder from(final DocumentSettings settings) {
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

        /**
         * @return A builder using the values from the default settings
         */
        public static Builder getDefaultBuilder() {
            return Builder.from(DocumentSettings.getDefault());
        }

        public static final class InvalidStateException extends Exception {
            /**
             * @param message
             */
            public InvalidStateException(final String message) {
                super(message);
            }
        }
    }
}