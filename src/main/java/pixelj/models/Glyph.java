package pixelj.models;

import pixelj.graphics.BinaryImage;
import pixelj.util.ChangeableInt;

/**
 * Two characters are equal if their code points are equal. Hash code of a
 * character is its code point.
 */
public class Glyph implements Comparable<Glyph> {
    public final ChangeableInt widthProperty;
    private final int codePoint;
    private final BinaryImage image;

    public Glyph(final int codePoint, final int width, final BinaryImage image) {
        this.codePoint = codePoint;
        widthProperty = new ChangeableInt(width);
        this.image = image;
    }

    @Override
    public int compareTo(final Glyph that) {
        return Integer.compare(codePoint, that.codePoint);
    }

    public int getCodePoint() {
        return codePoint;
    }

    public BinaryImage getImage() {
        return image;
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
        if (that instanceof Glyph other) {
            return codePoint == other.codePoint;
        }
        return false;
    }
}
