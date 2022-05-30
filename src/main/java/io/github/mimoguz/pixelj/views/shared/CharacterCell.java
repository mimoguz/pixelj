package io.github.mimoguz.pixelj.views.shared;

import java.awt.*;

import javax.swing.JLabel;
import javax.swing.JPanel;

import com.formdev.flatlaf.FlatClientProperties;

public class CharacterCell extends JPanel {
    private static final long serialVersionUID = -9001351906799070443L;

    private final JLabel letter = new JLabel();
    private final JLabel subtitle = new JLabel();
    private final JLabel title = new JLabel();

    public CharacterCell() {
        setOpaque(false);

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
        constraints.weighty = 0.0;
        constraints.anchor = GridBagConstraints.SOUTHWEST;
        add(subtitle, constraints);

        constraints.gridy = 1;
        constraints.weighty = 1.0;
        constraints.anchor = GridBagConstraints.NORTHWEST;
        add(title, constraints);
    }

    public void set(final int codePoint, final String title, final int maximumSize) {
        this.letter.setText(Character.toString((char) codePoint));
        this.title.setText(title);
        this.subtitle.setText("0x" + Integer.toHexString(codePoint));
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
