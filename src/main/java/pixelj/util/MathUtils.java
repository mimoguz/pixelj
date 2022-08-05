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
}
