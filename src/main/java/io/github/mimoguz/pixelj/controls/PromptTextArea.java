package io.github.mimoguz.pixelj.controls;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.util.Map;

import javax.swing.JTextArea;

import io.github.mimoguz.pixelj.resources.Resources;

/**
 * A text area control that can display a prompt text when it's empty and
 * unfocused.<br />
 * https://stackoverflow.com/questions/16213836/java-swing-jtextfield-set-placeholder
 */
public class PromptTextArea extends JTextArea {
    private static final long serialVersionUID = 4432702411987454931L;
    private static final Map<?, ?> hints = (Map<?, ?>) Toolkit.getDefaultToolkit()
            .getDesktopProperty("awt.font.desktophints");

    private String promptText;

    public String getPromptText() {
        return promptText;
    }

    public void setPromptText(String promptText) {
        this.promptText = promptText;
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
        g2d.setColor(Resources.get().colors.inactive());
        g2d.drawString(
                promptText,
                getInsets().left,
                graphics.getFontMetrics().getMaxAscent() + getInsets().top
        );
        g2d.dispose();
    }
}