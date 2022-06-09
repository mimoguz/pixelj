package io.github.mimoguz.pixelj.views.controls;

import java.awt.*;
import java.util.Map;

import javax.swing.JTextArea;

/**
 * A text area control that can display a prompt text when it's empty and
 * unfocused.<br />
 * <a href="https://stackoverflow.com/questions/16213836/java-swing-jtextfield-set-placeholder">...</a>
 */
public class PromptTextArea extends JTextArea {
    private static final Map<?, ?> hints = (Map<?, ?>) Toolkit.getDefaultToolkit()
            .getDesktopProperty("awt.font.desktophints");

    private Color promptColor;
    private String promptText;

    public Color getPromptColor() {
        return promptColor;
    }

    public String getPromptText() {
        return promptText;
    }

    @Override
    public void paintComponent(final Graphics graphics) {
        super.paintComponent(graphics);
        if (
            isFocusOwner() || (getText() != null && !getText().isEmpty()) || promptText == null
                    || promptText.isEmpty() || promptText.isBlank()
        ) {
            return;
        }
        final var g2d = (Graphics2D) graphics.create();
        if (hints != null) {
            g2d.setRenderingHints(hints);
        } else {
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        }
        g2d.setColor(promptColor != null ? promptColor : Color.GRAY);
        g2d.drawString(
                promptText,
                getInsets().left,
                graphics.getFontMetrics().getMaxAscent() + getInsets().top
        );
        g2d.dispose();
    }

    public void setPromptColor(final Color promptColor) {
        this.promptColor = promptColor;
    }

    public void setPromptText(final String promptText) {
        this.promptText = promptText;
    }
}
