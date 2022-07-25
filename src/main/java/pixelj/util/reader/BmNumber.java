package pixelj.util.reader;

import org.eclipse.collections.api.list.primitive.IntList;

public record BmNumber(int value) implements BmToken, BmValue {

    @Override
    public int getInt() {
        return value;
    }

    @Override
    public String getString() {
        throw new IllegalArgumentException("This is not a string.");
    }

    @Override
    public IntList getVector() {
        throw new IllegalArgumentException("This is not a vector.");
    }
}
