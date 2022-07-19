package pixelj.util.packer;

import java.util.ArrayList;
import java.util.List;

/**
 * Potentially more optimal packing than the grid packer. Still puts the rectangles into rows, but
 * unlike the grid packer, row heights and cell widths can vary.
 * Caution: The pack method modifies the input list.
 *
 * @param <M> Metadata type
 */
public final class RowPacker<M> implements Packer<M> {

    @Override
    public List<List<Rectangle<M>>> pack(
            final List<Rectangle<M>> input,
            final int boxWith,
            final int boxHeight
    ) {
        input.sort((a, b) -> Integer.compare(a.getHeight(), b.getHeight()));
        final ArrayList<List<Rectangle<M>>> result = new ArrayList<>();
        var lineStart = 0;
        var pageHeight = 0;
        ArrayList<Rectangle<M>> currentPage = new ArrayList<>();
        while (true) {
            final var line = getLine(input, boxWith, lineStart);

            // Create a new page if needed
            if (pageHeight + line.height() > boxHeight) {
                result.add(currentPage);
                currentPage = new ArrayList<>();
                pageHeight = 0;
            }

            // Add the line to the current page
            for (var i = line.start(); i < line.end(); i++) {
                currentPage.add(input.get(i));
            }
            pageHeight += line.height;

            // All rectangles were placed
            if (line.end() == input.size()) {
                break;
            }

            lineStart = line.end();
        }

        return result;
    }

    private Line<M> getLine(final List<Rectangle<M>> input, final int boxWidth, final int start) {
        var end = start;
        var width = 0;
        var height = 0;
        while (width <= boxWidth) {
            final var rect = input.get(start);
            final var w = width + rect.getWidth();
            if (w > boxWidth) {
                break;
            }
            width = w;
            height = Math.max(height, rect.getHeight());
            end++;
        }
        return new Line<>(height, start, end);
    }

    private record Line<T>(int height, int start, int end) {
    }
}
