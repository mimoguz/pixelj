package pixelj.util;

public final class MathUtils {

    private MathUtils() {
    }

    /**
     * I need to use Java 17 for now, because GitHub actions doesn't support Java 18. So, no Math.ceilDiv 
     * 
     * @param a Dividend
     * @param b Divisor
     * @return Ceil division
     */
    public static int ceilDiv(final int a, final int b) {
        return (int) Math.ceil((double) a / b);
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
