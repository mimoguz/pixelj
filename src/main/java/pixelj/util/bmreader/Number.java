package pixelj.util.bmreader;

public record Number(int value) implements Token, Value {

    @Override
    public int getInt() throws ReaderException {
        return value;
    }
}
