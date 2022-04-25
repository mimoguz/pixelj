package io.github.mimoguz.pixelj.models;

public class KerningPairModel extends MutableIntValueModel implements Comparable<KerningPairModel> {
    private int kerningValue;
    private final CharacterModel left;
    private final CharacterModel right;

    public KerningPairModel(CharacterModel left, CharacterModel right, int kerningValue) {
        this.left = left;
        this.right = right;
        this.kerningValue = kerningValue;
    }

    @Override
    public int compareTo(KerningPairModel that) {
        final var leftOrder = Integer.compare(left.getCodePoint(), that.right.getCodePoint());
        if (leftOrder == 0) {
            return Integer.compare(right.getCodePoint(), that.right.getCodePoint());
        } else {
            return leftOrder;
        }
    }

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that instanceof KerningPairModel other) {
            return left.equals(other.left) && right.equals(other.right);
        }
        return false;
    }

    public int getKerningValue() {
        return kerningValue;
    }

    public CharacterModel getLeft() {
        return left;
    }

    public CharacterModel getRight() {
        return right;
    }

    @Override
    public int hashCode() {
        // >In the Unicode Standard, the codespace consists of the integers from 0 to
        // 10FFFF<
        // 10FFFF occupies 21 bits, there shouldn't be any collisions here.
        return (left.getCodePoint() << 24) | right.getCodePoint();
    }

    public void setKerningValue(int value) {
        if (value == kerningValue) {
            return;
        }
        final var event = new IntValueChangeEvent(kerningValue, value);
        kerningValue = value;
        fireChangeEvent(this, event);
    }
}
