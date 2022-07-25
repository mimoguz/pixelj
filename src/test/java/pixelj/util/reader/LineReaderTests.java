package pixelj.util.reader;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;

public class LineReaderTests {

    /** Test char line. */
    @Test
    public void readCharLine() {
        final var line = "char id=45 x=0 y=0 width=5 height=1 xoffset=0 yoffset=11 xadvance=6 page=0 chnl=15";
        final var result = new LineReader(line).build().assignments();
        assertEquals(new BmNumber(45), result.get("id"));
        assertEquals(new BmNumber(0), result.get("x"));
        assertEquals(new BmNumber(0), result.get("y"));
        assertEquals(new BmNumber(5), result.get("width"));
        assertEquals(new BmNumber(1), result.get("height"));
        assertEquals(new BmNumber(0), result.get("xoffset"));
        assertEquals(new BmNumber(11), result.get("yoffset"));
        assertEquals(new BmNumber(6), result.get("xadvance"));
        assertEquals(new BmNumber(0), result.get("page"));
        assertEquals(new BmNumber(15), result.get("chnl"));
    }

    /** Test info line. */
    @Test
    public void readInfoLine() {
        final var line = "info face=\"My Font\" size=-11 bold=0 italic=0 unicode=1 stretchH=100 smooth=0"
                + " aa=1 padding=1,2,3,4 spacing=1,1 outline=0";
        final var result = new LineReader(line).build();
        assertEquals(BmKeyword.INFO.getText(), result.line().getText());
        assertEquals(new BmNumber(-11), result.assignments().get("size"));
        assertEquals(
                new BmVector(List.of(
                        new BmNumber(1),
                        new BmNumber(2),
                        new BmNumber(3),
                        new BmNumber(4))
                ),
                result.assignments().get("padding")
        );
    }
}
