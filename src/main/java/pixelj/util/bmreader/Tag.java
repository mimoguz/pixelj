package pixelj.util.bmreader;

import java.util.Map;

public record Tag(Ident tagType, Map<Ident, Value> assignments) {
}
