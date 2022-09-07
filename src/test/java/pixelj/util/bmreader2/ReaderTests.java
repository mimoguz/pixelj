package pixelj.util.bmreader2;

import org.junit.jupiter.api.Test;

public class ReaderTests {

    /** Test char tag. */
    @Test
    public void readCharLine() {
        final var line = "info face=\"Pix Mini\" size=-6 bold=0 italic=0 unicode=1 stretchH=100 smooth=0 "
            + "aa=1 padding=0,0,0,0 spacing=1,1 outline=0";
        try {
            final var tokens = new Scanner(line).scan();
            final var tag = new Parser(tokens).parse();
            final var tagStr = tag.toString();
            System.err.println(tagStr);
        } catch (ReadError e) {
            throw new AssertionError(e);
        }
    }
}
