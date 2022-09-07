package pixelj.util.bmreader2;

import org.eclipse.collections.api.list.primitive.IntList;

public sealed interface Value permits Value.Text, Value.Numbers {

    record Numbers(IntList values) implements Value { }

    record Text(String value) implements Value { }
}
