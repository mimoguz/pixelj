package pixelj.util.reader;

import java.util.List;

public record BmVector(List<BmNumber> values) implements BmToken, BmValue {
}
