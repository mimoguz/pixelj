package io.github.mimoguz.pixelj.models;

import io.github.mimoguz.pixelj.graphics.BinaryImage;

public class CharacterModel extends MutableIntValueModel implements Comparable<CharacterModel> {
    private final int codePoint;
    private final BinaryImage glyph;
    private int width;

    public CharacterModel(final int codePoint, final int width, final BinaryImage glyph) {
        this.codePoint = codePoint;
        this.width = width;
        this.glyph = glyph;
    }

    @Override
    public int compareTo(final CharacterModel that) {
        return Integer.compare(codePoint, that.codePoint);
    }

    @Override
    public boolean equals(final Object that) {
        if (this == that) {
            return true;
        }
        if (that instanceof CharacterModel other) {
            return codePoint == other.codePoint;
        }
        return false;
    }

    public int getCodePoint() {
        return codePoint;
    }

    public BinaryImage getGlyph() {
        return glyph;
    }

    public int getWidth() {
        return width;
    }

    @Override
    public int hashCode() {
        return codePoint;
    }

    public void setWidth(final int value) {
        if (value == width) {
            return;
        }
        final var event = new IntValueChangeEvent(width, value);
        width = value;
        fireChangeEvent(this, event);
    }
}
