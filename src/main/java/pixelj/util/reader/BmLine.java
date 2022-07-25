package pixelj.util.reader;

import java.util.Map;

public record BmLine(BmKeyword line, Map<String, BmValue> assignments) {
}
