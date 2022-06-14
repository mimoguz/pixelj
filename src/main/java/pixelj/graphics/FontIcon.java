package pixelj.graphics;

import java.awt.*;
import java.awt.image.BufferedImage;

import javax.swing.Icon;

public class FontIcon implements Icon {
    private Color disabledForeground;
    private final Font font;
    private Color foreground;
    private final int height;
    private final String symbol;
    private final int width;

    public FontIcon(
            final int codePoint,
            final Color foreground,
            final Color disabledForeground,
            final Font font
    ) {
        this.foreground = foreground;
        this.disabledForeground = disabledForeground;
        this.font = font;
        symbol = new String(new int[] { codePoint }, 0, 1);

        // Get symbol metrics:
        final var graphics = GraphicsEnvironment.getLocalGraphicsEnvironment()
                .createGraphics(
                        new BufferedImage(font.getSize(), font.getSize(), BufferedImage.TYPE_INT_ARGB)
                );
        graphics.setFont(font);
        final var metrics = graphics.getFontMetrics();
        height = metrics.getHeight();
        width = metrics.charWidth(codePoint);
    }

    /**
     * @return Color or null
     */
    public Color getDisabledForeground() {
        return disabledForeground;
    }

    /**
     * @return Color or null
     */
    public Color getForeground() {
        return foreground;
    }

    @Override
    public int getIconHeight() {
        return height;
    }

    @Override
    public int getIconWidth() {
        return width;
    }

    @Override
    public void paintIcon(final Component component, final Graphics graphics, final int x, final int y) {
        final var g = (Graphics2D) graphics.create();
        g.setFont(font);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(getPaint(component));
        g.drawString(symbol, x, y + g.getFontMetrics().getAscent());
        g.dispose();
    }

    /**
     * @param value Color or null
     */
    public void setDisabledForeground(Color value) {
        disabledForeground = value;
    }

    /**
     * @param value Color or null
     */
    public void setForeground(Color value) {
        foreground = value;
    }

    private Color getPaint(final Component component) {
        if (component.isEnabled()) {
            return foreground != null ? foreground : component.getForeground();
        } else {
            return disabledForeground != null ? disabledForeground : component.getForeground();
        }
    }
}
