package pixelj.util.bmreader;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/** BMFont config file reader. It doesn't check the validity of the tags. */
public final class Reader {

    private final Map<Ident, List<Tag>> lines;

    /**
     * @param input Config string
     * @throws IllegalArgumentException If the input cannot be parsed
     */
    public Reader(final String input) {
        lines = input.lines()
            .filter(line -> !(line.isBlank()))
            .map(line -> {
                try {
                    return new TagReader(line).read();
                } catch (ReaderException exception) {
                    throw new IllegalArgumentException(
                        "Error parsing input: " + exception.getMessage(),
                        exception.getCause()
                    );
                }
            })
            .collect(Collectors.groupingBy(line -> line.tagType()));
    }

    public List<Tag> getTags(final Ident line) {
        return lines.get(line);
    }
}
