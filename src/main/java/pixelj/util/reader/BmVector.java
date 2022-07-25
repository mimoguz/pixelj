package pixelj.util.reader;

import java.util.List;

import org.eclipse.collections.api.list.primitive.IntList;
import org.eclipse.collections.impl.list.mutable.primitive.IntArrayList;

public record BmVector(List<BmNumber> values) implements BmToken, BmValue {

    @Override
    public int getInt() {
        throw new IllegalArgumentException("This is not an int.");
    }

    @Override
    public String getString() {
        throw new IllegalArgumentException("This is not a string.");
    }

    @Override
    public IntList getVector() {
        final var result = new IntArrayList(values.size());
        for (var value : values) {
            result.add(value.value());
        }
        return result;
    }
}
