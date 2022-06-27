package pixelj.util.packer;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;

public class GridPackerTests {
    @Test
    public void square() {
        final var cols = 4;
        final var width = 10;
        final var rectangles = IntStream.range(0, cols * cols)
                .mapToObj(i -> new Rectangle(i, width, width))
                .toList();
        final var packer = new GridPacker(rectangles);
        final var size = packer.packInPlace(false, false);
        assertEquals(size.width, size.height);
        assertEquals(size.width, cols * width);
    }

    @Test
    public void stillSquare() {
        final var cols = 4;
        final var rows = 4;
        final var width = 10;
        final var rectangles = IntStream.range(0, cols * (rows - 1) + 1)
                .mapToObj(i -> new Rectangle(i, width, width))
                .toList();
        final var packer = new GridPacker(rectangles);
        final var size = packer.packInPlace(false, false);
        assertEquals(size.width, size.height);
        assertEquals(size.width, cols * width);
    }

    public void preferWide() {
        final var n = 15;
        final var width = 7;
        final var height = 20;
        final var rectangles = IntStream.range(0, n).mapToObj(i -> new Rectangle(i, width, height)).toList();
        final var packer = new GridPacker(rectangles);
        final var size = packer.packInPlace(false, false);
        System.out.println(size);
        assertEquals(size.width, 56); // 8 columns
        assertEquals(size.height, 40); // 2 rows
    }

    @Test
    public void powerOfTwoAndSquare() {
        final var n = 15;
        final var width = 7;
        final var height = 20;
        final var rectangles = IntStream.range(0, n).mapToObj(i -> new Rectangle(i, width, height)).toList();
        final var packer = new GridPacker(rectangles);
        final var size = packer.packInPlace(true, true);
        System.out.println(size);
        assertEquals(64, size.width);
        assertEquals(64, size.height);
    }
}
