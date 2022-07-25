package pixelj.util.reader;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public final class Reader {

    private final Map<BmKeyword, List<BmLine>> lines;

    public Reader(final String input) {
        lines = input.lines()
                .filter(line -> !(line.isBlank()))
                .map(line -> new LineReader(line).build())
                .collect(Collectors.groupingBy(line -> line.line()));
    }

    public List<BmLine> getLines(final BmKeyword line) {
        return lines.get(line);
    }
}
