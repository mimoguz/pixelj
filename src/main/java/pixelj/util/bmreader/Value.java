package pixelj.util.bmreader;

import org.eclipse.collections.api.list.primitive.IntList;

public sealed interface Value permits Number, NumberList, Text {

    default int getInt() throws ReaderException {
        throw new ReaderException("This is not an int.");
    }

    default IntList getIntList() throws ReaderException {
        throw new ReaderException("This is not an int list.");
    }

    default String getString() throws ReaderException {
        throw new ReaderException("This is not a string.");
    }
}
