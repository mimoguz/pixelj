package io.github.mimoguz.pixelj.models;

import io.github.mimoguz.pixelj.graphics.BinaryImage;

import org.jetbrains.annotations.NotNull;

public class CharacterModel extends MutableIntValueModel implements Comparable<CharacterModel> {
    private final int codePoint;
    private final @NotNull BinaryImage glyph;
    private int width;

    public CharacterModel(int codePoint, int width, @NotNull BinaryImage glyph) {
        this.codePoint = codePoint;
        this.width = width;
        this.glyph = glyph;
    }

    @Override
    public int compareTo(@NotNull CharacterModel that) {
        return Integer.compare(codePoint, that.codePoint);
    }

    @Override
    public boolean equals(Object that) {
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

    public @NotNull BinaryImage getGlyph() {
        return glyph;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int value) {
        if (value == width) {
            return;
        }
        final var event = new IntValueChangeEvent(width, value);
        width = value;
        fireChangeEvent(this, event);
    }

    @Override
    public int hashCode() {
        return codePoint;
    }
}
