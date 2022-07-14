package pixelj.models;

import pixelj.util.ChangeableBoolean;
import pixelj.util.ChangeableInt;
import pixelj.util.ChangeableValue;
import pixelj.util.ReadOnlyBoolean;
import pixelj.util.ReadOnlyInt;

public record DocumentSettings(
        String title,
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
        return new DocumentSettings("My Font", 24, 24, 16, 4, 11, 7, 7, 1, 4, 2, false, false, false);
    }

    public static final class Builder {

        /**
         * Document title.
         */
        public final ChangeableValue<String> title = new ChangeableValue<>(null);
        /**
         * From baseline to highest possible point of a glyph.
         */
        public final ChangeableInt ascender = new ChangeableInt(0);
        /**
         * Canvas height.
         */
        public final ChangeableInt canvasHeight = new ChangeableInt(0);
        /**
         * Canvas width.
         */
        public final ChangeableInt canvasWidth = new ChangeableInt(0);
        /**
         * Height of the capital letters, not counting accents.
         */
        public final ChangeableInt capHeight = new ChangeableInt(0);
        /**
         * Height of the lowercase x.
         */
        public final ChangeableInt xHeight = new ChangeableInt(0);
        /**
         * Default glyph width.
         */
        public final ChangeableInt defaultWidth = new ChangeableInt(0);
        /**
         * From baseline to lowest possible point of a glyph.
         */
        public final ChangeableInt descender = new ChangeableInt(0);
        /**
         * Is the font monospaced?
         */
        public final ChangeableBoolean isMonospaced = new ChangeableBoolean(false);
        /**
         * Extra line spacing.
         */
        public final ChangeableInt lineSpacing = new ChangeableInt(0);
        /**
         * Size of the space character.
         */
        public final ChangeableInt spaceSize = new ChangeableInt(0);
        /**
         * The space between two adjacent letters.
         */
        public final ChangeableInt letterSpacing = new ChangeableInt(0);
        /**
         * Font weight.
         */
        public final ChangeableBoolean isBold = new ChangeableBoolean(false);
        /**
         * Font style.
         */
        public final ChangeableBoolean isItalic = new ChangeableBoolean(false);

        /**
         * Check title.
         */
        public final ReadOnlyBoolean validTitle = title.test(t -> t != null && !t.isBlank());
        /**
         * Check ascender.
         */
        public final ReadOnlyBoolean validAscender = new ReadOnlyInt(descender)
                .le(canvasHeight.subtract(ascender))
                .and(ascender.gt(0));
        /**
         * Check canvas height.
         */
        public final ReadOnlyBoolean validCanvasHeight = canvasHeight.gt(0);
        /**
         * Check canvas width.
         */
        public final ReadOnlyBoolean validCanvasWidth = canvasWidth.gt(0);
        /**
         * Check cap height.
         */
        public final ReadOnlyBoolean validCapHeight = capHeight.le(ascender).and(capHeight.gt(0));
        /**
         * Check x height.
         */
        public final ReadOnlyBoolean validXHeight = xHeight.le(capHeight).and(xHeight.gt(0));
        /**
         * Check default glyph width.
         */
        public final ReadOnlyBoolean validDefaultWidth = defaultWidth.le(canvasWidth);
        /**
         * Check descender.
         */
        public final ReadOnlyBoolean validDescender = descender.le(canvasHeight).and(descender.gt(0));
        /**
         * Check line spacing.
         */
        public final ReadOnlyBoolean validLineSpacing = lineSpacing.ge(0);
        /**
         * Check space size.
         */
        public final ReadOnlyBoolean validSpaceSize = spaceSize.ge(0);
        /**
         * Check letter spacing.
         */
        public final ReadOnlyBoolean validLetterSpacing = letterSpacing.ge(0);

        /**
         * Check all.
         */
        public final ReadOnlyBoolean validAll = validTitle
                .and(validCanvasHeight)
                .and(validCanvasWidth)
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
                    title.getValue(),
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
         * @param value Project title
         * @return This
         */
        public Builder setTitle(final String value) {
            title.setValue(value);
            return this;
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
         * Copy all settings from the source.
         *
         * @param settings Source
         * @return This
         */
        public Builder set(final DocumentSettings settings) {
            this
                    .setTitle(settings.title())
                    .setAscender(settings.ascender())
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

            return this;
        }

        /**
         * @param settings Source
         * @return A builder using the values from the source
         */
        public static Builder from(final DocumentSettings settings) {
            return new Builder().set(settings);
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
