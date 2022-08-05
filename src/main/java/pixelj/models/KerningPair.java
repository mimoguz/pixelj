package pixelj.models;

import pixelj.util.ChangeableInt;

/**
 * Two kerning pairs are equal if their left and right scalars are equal. Hash
 * code of a kerning pair is calculated over its left and right scalars using
 * getHash static method.
 */
public final class KerningPair implements Comparable<KerningPair>, HasId {
    /**
     * Kerning value.
     */
    public final ChangeableInt kerningValueProperty;

    private final Glyph left;
    private final Glyph right;

    public KerningPair(final Glyph left, final Glyph right, final int kerningValue) {
        this.left = left;
        this.right = right;
        kerningValueProperty = new ChangeableInt(kerningValue);
    }

    public int getKerningValue() {
        return kerningValueProperty.getValue();
    }

    public void setKerningValue(final int value) {
        kerningValueProperty.setValue(value);
    }

    public Glyph getLeft() {
        return left;
    }

    public Glyph getRight() {
        return right;
    }

    @Override
    public long getId() {
        return calculateId(left.getCodePoint(), right.getCodePoint());
    }

    @Override
    public int compareTo(final KerningPair that) {
        final var leftOrder = Integer.compare(left.getCodePoint(), that.left.getCodePoint());
        if (leftOrder == 0) {
            return Integer.compare(right.getCodePoint(), that.right.getCodePoint());
        } else {
            return leftOrder;
        }
    }

    @Override
    public int hashCode() {
        return (left.hashCode() << 31) | right.hashCode();
    }

    @Override
    public boolean equals(final Object that) {
        if (this == that) {
            return true;
        }
        if (that instanceof final KerningPair other) {
            return left.equals(other.left) && right.equals(other.right);
        }
        return false;
    }

    /**
     * To calculate id of a kerning pair without creating one.
     *
     * @param leftCodePoint
     * @param rightCodePoint
     * @return Long hash code
     */
    public static long calculateId(final int leftCodePoint, final int rightCodePoint) {
        // >In the Unicode Standard, the codespace consists of the integers from 0 to
        // 0x10FFFF.<
        // 0x10FFFF occupies 21 bits, there shouldn't be any collisions here.
        return (((long) leftCodePoint) << 31) | rightCodePoint;
    }
}
