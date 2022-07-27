package pixelj.util.bmreader;

import java.util.List;

import org.eclipse.collections.api.list.primitive.IntList;
import org.eclipse.collections.impl.list.mutable.primitive.IntArrayList;

public record NumberList(List<Number> values) implements Value {

    @Override
    public IntList getIntList() throws ReaderException {
        final var result = new IntArrayList(values.size());
        for (var value : values) {
            result.add(value.value());
        }
        return result;
    }
}
