package pixelj.util.reader;

import java.util.Map;

public record BmLine(BmField line, Map<String, BmValue> assignments) {
}
