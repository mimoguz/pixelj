package pixelj.models;

import pixelj.util.ChangeableInt;

/**
 * Two kerning pairs are equal if their left and right characters are equal.
 * Hash code of a kerning pair is calculated over its left and right characters
 * using getHash static method.
 */
public class KerningPair implements Comparable<KerningPair> {
    private final CharacterItem left;
    private final CharacterItem right;
    public ChangeableInt kerningValueProperty;

    public KerningPair(final CharacterItem left, final CharacterItem right, final int kerningValue) {
        this.left = left;
        this.right = right;
        kerningValueProperty = new ChangeableInt(kerningValue);
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

    public int getKerningValue() {
        return kerningValueProperty.getValue();
    }

    public void setKerningValue(final int value) {
        kerningValueProperty.setValue(value);
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
     * This is essentially a hack. I use this method to calculate a hash without
     * creating an instance, then use that hash to search a model in a
     * KerningPairListModel.
     */
    public static int getHash(final CharacterItem left, final CharacterItem right) {
        // >In the Unicode Standard, the codespace consists of the integers from 0 to
        // 0x10FFFF.<
        // 0x10FFFF occupies 21 bits, there shouldn't be any collisions here.
        return (left.hashCode() << 24) | right.hashCode();
    }
}
