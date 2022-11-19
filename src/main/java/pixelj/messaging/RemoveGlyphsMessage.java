package pixelj.messaging;

import pixelj.models.Glyph;

import java.util.Collection;

public record RemoveGlyphsMessage(Collection<Glyph> glyphs) {
}
