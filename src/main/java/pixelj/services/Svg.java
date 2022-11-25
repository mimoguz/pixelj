package pixelj.services;

import pixelj.models.DocumentSettings;
import pixelj.models.Glyph;

import java.awt.geom.Area;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

import static java.awt.geom.PathIterator.SEG_CLOSE;
import static java.awt.geom.PathIterator.SEG_LINETO;
import static java.awt.geom.PathIterator.SEG_MOVETO;

public class Svg {
    public static final int UNITS_PER_PIXEL = 100;

    private final int width;
    private final int height;
    private final int id;
    private final String path;

    public Svg(final Glyph glyph, DocumentSettings prefs) {
        width = prefs.isMonospaced() ? prefs.defaultWidth() : glyph.getWidth();
        height = prefs.ascender() + prefs.descender() + prefs.lineSpacing();
        id = glyph.getCodePoint();

        final var area = toArea(glyph, prefs.canvasHeight() - height, height, width, prefs.lineSpacing());
        final var nodes = reduce(area);
        final var builder = new StringBuilder();
        for (var node : nodes) {
            builder.append(node.str()).append(' ');
        }
        path = builder.toString();
    }

    public int getId() {
        return id;
    }

    public int getWidth() {
        return width * UNITS_PER_PIXEL;
    }

    public int getHeight() {
        return height * UNITS_PER_PIXEL;
    }

    public String getXml() {
        final var builder = new StringBuilder();
        // @formatter:off
        builder
            .append("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n")
            .append("<svg width=\"").append(getWidth()).append("\"\n")
            .append("     height=\"").append(getHeight()).append("\"\n")
            .append("     viewBox=\"0 0 ").append(getWidth()).append(' ').append(getHeight()).append("\"\n")
            .append("     version=\"1.1\"\n")
            .append("     id=\"g").append(id).append("\"\n")
            .append("     xmlns=\"http://www.w3.org/2000/svg\"\n")
            .append("     xmlns:svg=\"http://www.w3.org/2000/svg\">\n")
            .append("  <path id=\"shape\" style=\"fill:#000000;\"\n")
            .append("        d=\"").append(path).append("\"/>\n")
            .append("</svg>\n");
        // @formatter:on
        return builder.toString();
    }

    private static Area toArea(
        final Glyph glyph,
        final int y0,
        final int height,
        final int width,
        final int bottomPadding
    ) {
        final var image = glyph.getImage();
        final var path = new Path2D.Double();
        for (var y = 0; y < height; y++) {
            for (var x = 0; x < width; x++) {
                // Glyph pixel is filled if the image pixel is not.
                if (!image.get(x, y + y0)) {
                    path.append(new Rectangle2D.Double(
                        x * UNITS_PER_PIXEL,
                        (y - bottomPadding) * UNITS_PER_PIXEL,
                        UNITS_PER_PIXEL,
                        UNITS_PER_PIXEL
                    ), false);
                }
            }
        }

        return new Area(path);
    }

    private static List<Node> reduce(final Area area) {
        final var nodes = new ArrayList<Node>();
        final var iterator = area.getPathIterator(null);
        final var coords = new double[2];

        while (!iterator.isDone()) {
            final var node = switch (iterator.currentSegment(coords)) {
                case SEG_MOVETO -> new Node.MoveTo(coords[0], coords[1]);
                case SEG_LINETO -> new Node.LineTo(coords[0], coords[1]);
                case SEG_CLOSE -> new Node.Close();
                default -> new Node.Unsupported();
            };

            final var count = nodes.size();
            if (count >= 2) {
                final var n1 = nodes.get(count - 1);
                final var n2 = nodes.get(count - 2);
                if ((n1 instanceof Node.MoveTo m1) &&
                    (n2 instanceof Node.LineTo l2) &&
                    (node instanceof Node.LineTo l3)) {
                    if (sameLine(m1.x, m1.y, l2.x, l2.y, l3.x, l3.y)) {
                        nodes.remove(count - 1);
                    }
                }
                if ((n1 instanceof Node.LineTo l1) &&
                    (n2 instanceof Node.LineTo l2) &&
                    (node instanceof Node.LineTo l3)) {
                    if (sameLine(l1.x, l1.y, l2.x, l2.y, l3.x, l3.y)) {
                        nodes.remove(count - 1);
                    }
                }
            }
            nodes.add(node);
            iterator.next();
        }

        return nodes;
    }

    private static boolean sameLine(
        // @formatter:off
        final double p1x, final double p1y,
        final double p2x, final double p2y,
        final double p3x, final double p3y
        // @formatter:on
    ) {
        return (almostEqual(p1x, p2x) && almostEqual(p2x, p3x)) || (almostEqual(p1y, p2y) && almostEqual(p2y, p3y));
    }

    private static boolean almostEqual(final double a, final double b) {
        return Math.abs(a - b) <= 0.001;
    }

    private sealed interface Node {
        String str();

        record MoveTo(Double x, Double y) implements Node {
            public String str() {
                return "M" + x + ' ' + y;
            }
        }

        record LineTo(Double x, Double y) implements Node {
            public String str() {
                return "L" + x + ' ' + y;
            }
        }

        record Close() implements Node {
            public String str() {
                return "Z";
            }
        }

        record Unsupported() implements Node {
            public String str() {
                return "";
            }
        }
    }
}
