package pixelj.util.bmreader2;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public final class Reader {

    private final Map<Word, List<Map<Word, Value>>> lines;

    /**
     * @param input Config string
     * @throws IllegalArgumentException If the input cannot be parsed
     */
    public Reader(final String input) {
        lines = input.lines()
            .filter(line -> !(line.isBlank()))
            .map(line -> {
                try {
                    return new Parser(new Scanner(line).scan()).parse();
                } catch (ReadError error) {
                    throw new IllegalArgumentException(
                        "Error parsing input: " + error.getMessage(),
                        error.getCause()
                    );
                }
            })
            .collect(Collectors.groupingBy(
                Parser.Result::title,
                Collectors.mapping(Parser.Result::values, Collectors.toList()))
            );
    }

    /**
     * @param tag BMFont config tag
     * @return Maps of all (word, value) pairs for each line for given tag
     * @throws IllegalArgumentException If the tag was not in the input
     */
    public List<Map<Word, Value>> getTags(final Word tag) throws IllegalArgumentException {
        if (!lines.containsKey(tag)) {
            throw new IllegalArgumentException(tag.getText() + "  cannot be found");
        }
        return lines.get(tag);
    }
}
