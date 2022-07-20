package pixelj.util.packer;

import java.util.ArrayList;
import java.util.List;

/**
 * Potentially more optimal packing than the grid packer. Still puts the rectangles into rows, but
 * unlike the grid packer, row heights and cell widths can vary.
 *
 * @param <M> Metadata type
 */
public final class RowPacker<M> implements Packer<M> {

    @Override
    public List<List<Rectangle<M>>> pack(
            final ArrayList<Rectangle<M>> input,
            final int boxWith,
            final int boxHeight
    ) {
        input.sort((a, b) -> Integer.compare(a.getHeight(), b.getHeight()));

        final ArrayList<List<Rectangle<M>>> result = new ArrayList<>();
        var lineStart = 0;
        var pageHeight = 0;
        ArrayList<Rectangle<M>> currentPage = new ArrayList<>();
        result.add(currentPage);
        while (lineStart < input.size()) {
            final var line = getLine(input, boxWith, lineStart);

            // Create a new page if needed
            if (pageHeight + line.height() > boxHeight) {
                currentPage = new ArrayList<>();
                result.add(currentPage);
                pageHeight = 0;
            }

            // Move line's rectangles and add them to the current page
            var x = 0;
            for (var i = line.start(); i < line.end(); i++) {
                final var rect = input.get(i);
                rect.moveTo(x, pageHeight);
                x += rect.getWidth();
                currentPage.add(rect);
            }

            pageHeight += line.height();
            lineStart = line.end();
        }

        return result;
    }

    private Line getLine(final List<Rectangle<M>> input, final int boxWidth, final int start) {
        var end = start;
        var width = 0;
        var height = 0;
        while (width <= boxWidth && end < input.size()) {
            final var rect = input.get(end);
            final var w = width + rect.getWidth();
            if (w > boxWidth) {
                break;
            }
            width = w;
            height = Math.max(height, rect.getHeight());
            end++;
        }
        return new Line(height, start, end);
    }

    private record Line(int height, int start, int end) {
    }
}
