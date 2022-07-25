package pixelj.util.reader;

public final class ReaderException extends Exception {

    public ReaderException(final String message) {
        super(message);
    }

    public ReaderException(final Exception e) {
        super(e);
    }
}
