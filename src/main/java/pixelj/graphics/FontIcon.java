package pixelj.graphics;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import javax.swing.Icon;

public final class FontIcon implements Icon {
    private Color disabledForeground;
    private Color foreground;
    private final Font font;
    private final String symbol;
    private final int height;
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
        symbol = new String(new int[] {codePoint}, 0, 1);

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
    public void setDisabledForeground(final Color value) {
        disabledForeground = value;
    }

    /**
     * @param value Color or null
     */
    public void setForeground(final Color value) {
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
