package pixelj.util.reader;

public final class BmEquals implements BmToken {
    private static final BmEquals INSTANCE = new BmEquals();

    private BmEquals() {
    }

    /**
     * @return Instance
     */
    public static BmEquals get() {
        return INSTANCE;
    }
}
