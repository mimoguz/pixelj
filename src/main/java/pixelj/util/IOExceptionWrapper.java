package pixelj.util;

import java.io.IOException;

public class IOExceptionWrapper extends RuntimeException {
    public IOExceptionWrapper(final IOException exception) {
        super(exception);
    }
}
