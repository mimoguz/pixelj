package pixelj.messaging;

import pixelj.models.Glyph;

import java.util.Collection;
import java.util.List;

public record RemoveGlyphsMessage(Collection<Glyph> glyphs) {
}
