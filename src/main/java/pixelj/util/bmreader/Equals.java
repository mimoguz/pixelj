package pixelj.util.bmreader;

public final class Equals implements Token {

    private static final Equals INSTANCE = new Equals();

    private Equals() {
    }

    /**
     * @return Singleton instance
     */
    public static Equals get() {
        return INSTANCE;
    }
}
