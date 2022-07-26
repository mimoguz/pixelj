package pixelj.util.bmreader;

public record Text(String value) implements Token, Value {

    @Override
    public String getString() throws ReaderException {
        return value;
    }
}
