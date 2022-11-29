package pixelj.util.bmreader2;

import it.unimi.dsi.fastutil.ints.IntArrayList;

public sealed interface Value permits Value.Text, Value.Numbers {

    record Numbers(IntArrayList values) implements Value { }

    record Text(String value) implements Value { }
}
