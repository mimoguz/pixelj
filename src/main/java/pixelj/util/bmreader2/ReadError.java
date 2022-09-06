package pixelj.util.bmreader2;

final class ReadError extends Exception {

    ReadError(final String msg) {
        super(msg);
    }

    ReadError(final Exception e) {
        super(e);
    }
}
