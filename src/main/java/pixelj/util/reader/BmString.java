package pixelj.util.reader;

import org.eclipse.collections.api.list.primitive.IntList;

public record BmString(String value) implements BmToken, BmValue {

    @Override
    public int getInt() {
        throw new IllegalArgumentException("This is not an int.");
    }

    @Override
    public String getString() {
        return value;
    }

    @Override
    public IntList getVector() {
        throw new IllegalArgumentException("This is not a vector.");
    }
}
