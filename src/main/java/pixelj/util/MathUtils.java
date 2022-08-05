package pixelj.util;

public final class MathUtils {

    private MathUtils() {
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

    /**
     * @param value
     * @param min   Minimum value, inclusive
     * @param max   Maximum value, inclusive
     * @return      value if min ≤ value ≤ max, min if value ≤ min, max if max ≤ value.
     */
    public static int clamp(final int value, final int min, final int max) {
        return Math.max(Math.min(value, max), min);
    }
}
