package pixelj.util.reader;

public interface LineReader2<T extends Line> {

    /**
     * Read and parse the line.
     *
     * @param input
     * @return Parsed line
     * @throws ReaderException
     */
    T read(String input) throws ReaderException;
}
