package pixelj.messaging;

import pixelj.models.Glyph;

public record AddKerningPairMessage(Glyph left, Glyph right, boolean addMirror) {
}
