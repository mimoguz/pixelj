package pixelj.util.bmreader2;

import it.unimi.dsi.fastutil.ints.IntArrayList;

import java.util.Map;

public final class Util {

    private static final String CANT_FIND = "Can't get the value of key ";
    private static final String VALUE_OF_KEY = "The value of key ";

    private Util() { }

    public static String getString(final Map<Word, Value> map, final Word key) throws ReadError {
        final var value = map.get(key);
        if (value == null) {
            throw new ReadError(CANT_FIND + key);
        }
        if (value instanceof Value.Text text) {
            return text.value();
        }
        throw new ReadError(VALUE_OF_KEY + key + "is not a string");
    }

    public static int getInt(final Map<Word, Value> map, final Word key) throws ReadError {
        final var value = map.get(key);
        if (value == null) {
            throw new ReadError(CANT_FIND + key);
        }
        if (value instanceof Value.Numbers num && num.values().size() == 1) {
            return num.values().getInt(0);
        }
        throw new ReadError(VALUE_OF_KEY + key + "is not an integer");
    }

    public static IntArrayList getIntList(final Map<Word, Value> map, final Word key, final int expectedSize)
        throws ReadError {
        final var value = map.get(key);
        if (value == null) {
            throw new ReadError(CANT_FIND + key);
        }
        if (value instanceof Value.Numbers ns) {
            if (ns.values().size() != expectedSize) {
                throw new ReadError(
                    "Integer list for the key " + key + " isn't " + expectedSize + " items long"
                );
            }
            return ns.values();
        }
        throw new ReadError(VALUE_OF_KEY + key + "is not an integer list");
    }
}
