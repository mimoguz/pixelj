package io.github.pixelj.graphics;

import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class FontIcon implements Icon {
    private final Font font;
    private final int height;
    private final String symbol;
    private final int width;
    private Color disabledForeground;
    private Color foreground;

    public FontIcon(int codePoint, @NotNull Color foreground, @NotNull Color disabledForeground, @NotNull Font font) {
        this.foreground = foreground;
        this.disabledForeground = disabledForeground;
        this.font = font;
        symbol = new String(new int[]{codePoint}, 0, 1);

        // Get symbol metrics:
        final var graphics =
                GraphicsEnvironment
                        .getLocalGraphicsEnvironment()
                        .createGraphics(new BufferedImage(font.getSize(), font.getSize(), BufferedImage.TYPE_INT_ARGB));
        graphics.setFont(font);
        final var metrics = graphics.getFontMetrics();
        height = metrics.getHeight();
        width = metrics.charWidth(codePoint);
    }

    public FontIcon(int codePoint, @NotNull Color foreground, @NotNull Font font) {
        this(codePoint, foreground, foreground, font);
    }

    public Color getDisabledForeground() {
        return disabledForeground;
    }

    public void setDisabledForeground(Color value) {
        disabledForeground = value;
    }

    @NotNull
    public Color getForeground() {
        return foreground;
    }

    public void setForeground(@NotNull Color value) {
        foreground = value;
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
    public void paintIcon(Component component, Graphics graphics, int x, int y) {
        final var g = (Graphics2D) graphics.create();
        g.setFont(font);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(component.isEnabled() ? foreground : disabledForeground);
        g.drawString(symbol, x, y + g.getFontMetrics().getAscent());
        g.dispose();
    }
}
