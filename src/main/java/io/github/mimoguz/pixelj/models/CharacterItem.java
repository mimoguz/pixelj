package io.github.mimoguz.pixelj.models;

import io.github.mimoguz.pixelj.graphics.BinaryImage;
import io.github.mimoguz.pixelj.util.ChangeableInt;

/**
 * Two characters are equal if their code points are equal.
 * Hash code of a character is its code point.
 */
public class CharacterItem implements Comparable<CharacterItem> {
    public final ChangeableInt widthProperty;
    private final int codePoint;
    private final BinaryImage glyph;

    public CharacterItem(final int codePoint, final int width, final BinaryImage glyph) {
        this.codePoint = codePoint;
        widthProperty = new ChangeableInt(width);
        this.glyph = glyph;
    }

    @Override
    public int compareTo(final CharacterItem that) {
        return Integer.compare(codePoint, that.codePoint);
    }

    public int getCodePoint() {
        return codePoint;
    }

    public BinaryImage getGlyph() {
        return glyph;
    }

    public int getWidth() {
        return widthProperty.getValue();
    }

    public void setWidth(final int value) {
        widthProperty.setValue(value);
    }

    @Override
    public int hashCode() {
        return codePoint;
    }

    @Override
    public boolean equals(final Object that) {
        if (this == that) {
            return true;
        }
        if (that instanceof CharacterItem other) {
            return codePoint == other.codePoint;
        }
        return false;
    }
}
