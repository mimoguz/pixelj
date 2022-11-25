package pixelj.services;

import org.junit.jupiter.api.Test;
import pixelj.graphics.BinaryImage;
import pixelj.models.DocumentSettings;
import pixelj.models.Glyph;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class SvgTests {
    @Test
    public void emptyPath() {
        final var prefs =  DocumentSettings.getDefault();
        final var glyph = new Glyph(65, 5, BinaryImage.of(prefs.canvasWidth(), prefs.canvasHeight(), true));
        final var svg = new Svg(glyph, prefs).getXml();
        final var w =  glyph.getWidth() * Svg.UNITS_PER_PIXEL;
        final var h =  (prefs.descender() + prefs.ascender()) * Svg.UNITS_PER_PIXEL;
        final var expected =
            // formatter:off
            "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n" +
            "<svg width=\""  + w + "\"\n" +
            "     height=\""  + h + "\"\n" +
            "     viewBox=\"0 0 "  + w  + ' ' + h  + "\"\n" +
            "     version=\"1.1\"\n" +
            "     id=\"g"  + glyph.getCodePoint()  + "\"\n" +
            "     xmlns=\"http://www.w3.org/2000/svg\"\n" +
            "     xmlns:svg=\"http://www.w3.org/2000/svg\">\n" +
            "  <path id=\"shape\" style=\"fill:#000000;\"\n" +
            "        d=\""  + ""  + "\"/>\n" +
            "</svg>\n";
            // formatter:on
        assertEquals(expected, svg);
    }
}
