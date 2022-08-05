package pixelj.models;

public record IdInt(int value) implements Comparable<IdInt>, HasId {
    /**
     * @param left
     * @param right
     * @return left < value < right
     */
    public boolean between(final IdInt left, final IdInt right) {
        return value > left.value() && value < right.value();
    }

    @Override
    public long getId() {
        return value;
    }

    @Override
    public int compareTo(final IdInt that) {
        return Integer.compare(value, that.value());
    }
}
