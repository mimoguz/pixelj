package pixelj.views.shared;

import java.awt.Color;

import javax.swing.JLabel;
import javax.swing.JPanel;

import com.formdev.flatlaf.FlatClientProperties;

import net.miginfocom.swing.MigLayout;

public final class ScalarCell extends JPanel {
    private final JLabel letter = new JLabel();
    private final JLabel subtitle = new JLabel();
    private final JLabel title = new JLabel();

    public ScalarCell() {
        Components.setFixedSize(letter, Dimensions.LETTER_BOX_SIZE);
        letter.putClientProperty(FlatClientProperties.STYLE_CLASS, "h3");
        subtitle.putClientProperty(FlatClientProperties.STYLE_CLASS, "small");

        setLayout(new MigLayout(
            "insets 4lp 0 4lp 0",
            String.format(
                "[center, %dlp!]%dlp[left, grow]",
                Dimensions.LETTER_BOX_SIZE.width,
                Dimensions.MEDIUM_PADDING
            ),
            String.format(
                "[bottom, %dlp!]0[top, %dlp!]",
                Dimensions.LETTER_BOX_SIZE.height / 2,
                Dimensions.LETTER_BOX_SIZE.height / 2
            )
        ));

        add(letter, "spany 2, center");
        add(subtitle, "cell 1 0, growx, wrap");
        add(title, "cell 1 1, growx");
    }

    /**
     * @param codePoint
     * @param titleText
     */
    public void set(final int codePoint, final String titleText) {
        letter.setText(Character.toString((char) codePoint));
        title.setText(titleText);
        subtitle.setText("0x" + Integer.toHexString(codePoint));
        // setMaximumSize(new Dimension(maximumSize, Integer.MAX_VALUE));
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
