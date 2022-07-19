package pixelj.util;

public final class MathUtils {

    private MathUtils() {
    }

    // TODO: Remove
    /**
     * This is a leftover from when I've used JDK 17.
     *
     * @param a Dividend
     * @param b Divisor
     * @return Ceil division
     */
    @Deprecated
    public static int ceilDiv(final int a, final int b) {
        return Math.ceilDiv(a, b);
    }

    /**
     * @param a
     * @return Is the number even
     */
    public static boolean even(final int a) {
        return (a & 1) == 0;
    }

    /**
     * @param a
     * @return Is the number even
     */
    public static boolean odd(final int a) {
        return (a & 1) == 1;
    }
}
