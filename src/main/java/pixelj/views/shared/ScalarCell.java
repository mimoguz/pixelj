package pixelj.views.shared;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JPanel;

import com.formdev.flatlaf.FlatClientProperties;

public final class ScalarCell extends JPanel {
    private final JLabel letter = new JLabel();
    private final JLabel subtitle = new JLabel();
    private final JLabel title = new JLabel();

    public ScalarCell() {
        Components.setFixedSize(letter, Dimensions.LETTER_BOX_SIZE);
        letter.putClientProperty(FlatClientProperties.STYLE_CLASS, "h3");
        subtitle.putClientProperty(FlatClientProperties.STYLE_CLASS, "small");

        setLayout(new GridBagLayout());
        final var constraints = new GridBagConstraints();

        constraints.insets = new Insets(0, 0, 0, Dimensions.MEDIUM_PADDING);
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridheight = 2;
        constraints.weightx = 0.0;
        constraints.anchor = GridBagConstraints.WEST;
        add(letter, constraints);

        constraints.insets = new Insets(0, 0, 0, 0);
        constraints.gridheight = 1;
        constraints.gridx = 1;
        constraints.gridy = 0;
        constraints.weightx = 1.0;
        constraints.weighty = 1.0;
        constraints.anchor = GridBagConstraints.SOUTHWEST;
        add(subtitle, constraints);

        constraints.gridy = 1;
        constraints.weighty = 1.0;
        constraints.anchor = GridBagConstraints.NORTHWEST;
        add(title, constraints);
    }

    /**
     * @param codePoint
     * @param titleText
     * @param maximumSize
     */
    public void set(final int codePoint, final String titleText, final int maximumSize) {
        letter.setText(Character.toString((char) codePoint));
        title.setText(titleText);
        subtitle.setText("0x" + Integer.toHexString(codePoint));
        setMaximumSize(new Dimension(maximumSize, Integer.MAX_VALUE));
    }

    public void setBackgroundColor(final Color color) {
        setBackground(color);
    }

    public void setForegroundColor(final Color color) {
        title.setForeground(color);
        subtitle.setForeground(color);
        letter.setForeground(color);
    }
}
