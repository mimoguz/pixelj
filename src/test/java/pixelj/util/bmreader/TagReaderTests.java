package pixelj.util.bmreader;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

public class TagReaderTests {

    /** Test char tag. */
    @Test
    public void readCharLine() {
        final var line = "char id=45 x=0 y=0 width=5 height=1 xoffset=0 yoffset=11 xadvance=6 page=0 chnl=15";
        final Map<Ident, Value> result;
        try {
            result = new TagReader(line).read().assignments();
        } catch (ReaderException exception) {
            throw new AssertionError(exception);
        }

        assertEquals(new Number(45), result.get(Ident.ID));
        assertEquals(new Number(0), result.get(Ident.X));
        assertEquals(new Number(0), result.get(Ident.Y));
        assertEquals(new Number(5), result.get(Ident.WIDTH));
        assertEquals(new Number(1), result.get(Ident.HEIGHT));
        assertEquals(new Number(0), result.get(Ident.X_OFFSET));
        assertEquals(new Number(11), result.get(Ident.Y_OFFSET));
        assertEquals(new Number(6), result.get(Ident.X_ADVANCE));
        assertEquals(new Number(0), result.get(Ident.PAGE));
        assertEquals(new Number(15), result.get(Ident.CHANNEL));
    }

    /** Test info tag. */
    @Test
    public void readInfoLine() {
        final var line = "info face=\"My Font\" size=-11 bold=0 italic=0 unicode=1 stretchH=100 smooth=0"
            + " aa=1 padding=1,2,3,4 spacing=1,1 outline=0";
        final Tag result;
        try {
            result = new TagReader(line).read();
        } catch (ReaderException exception) {
            throw new AssertionError(exception);
        }

        assertEquals(Ident.INFO.getText(), result.tagType().getText());
        assertEquals(new Text("My Font"), result.assignments().get(Ident.FACE));
        assertEquals(new Number(-11), result.assignments().get(Ident.SIZE));
        assertEquals(
            new NumberList(List.of(
                new Number(1),
                new Number(2),
                new Number(3),
                new Number(4))
            ),
            result.assignments().get(Ident.PADDING)
        );
    }
}
