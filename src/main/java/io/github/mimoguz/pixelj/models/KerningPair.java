package io.github.mimoguz.pixelj.models;

/**
 * Two kerning pairs are equal if their left and right characters are equal.
 * Hash code of a kerning pair is calculated over its left and right characters
 * using getHash static method.
 */
public class KerningPair extends MutableIntValueModel implements Comparable<KerningPair> {
    private int kerningValue;
    private final CharacterItem left;
    private final CharacterItem right;

    public KerningPair(final CharacterItem left, final CharacterItem right, final int kerningValue) {
        this.left = left;
        this.right = right;
        this.kerningValue = kerningValue;
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
    public boolean equals(final Object that) {
        if (this == that) {
            return true;
        }
        if (that instanceof final KerningPair other) {
            return left.equals(other.left) && right.equals(other.right);
        }
        return false;
    }

    public int getKerningValue() {
        return kerningValue;
    }

    public CharacterItem getLeft() {
        return left;
    }

    public CharacterItem getRight() {
        return right;
    }

    @Override
    public int hashCode() {
        return getHash(left, right);
    }

    public void setKerningValue(final int value) {
        if (value == kerningValue) {
            return;
        }
        final var event = new IntValueChangeEvent(kerningValue, value);
        kerningValue = value;
        fireChangeEvent(this, event);
    }

    /**
     * This is essentially a hack. I use this method to calculate a hash without
     * creating an instance, then use that hash to search a model in a
     * KerningPairListModel.
     */
    public static int getHash(final CharacterItem left, final CharacterItem right) {
        // >In the Unicode Standard, the codespace consists of the integers from 0 to
        // 10FFFF.<
        // 10FFFF occupies 21 bits, there shouldn't be any collisions here.
        return (left.hashCode() << 24) | right.hashCode();
    }
}
