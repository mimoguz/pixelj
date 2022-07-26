package pixelj.util.bmreader;

public final class ReaderException extends Exception {

    public ReaderException(final String message) {
        super(message);
    }

    public ReaderException(final Exception e) {
        super(e);
    }
}
