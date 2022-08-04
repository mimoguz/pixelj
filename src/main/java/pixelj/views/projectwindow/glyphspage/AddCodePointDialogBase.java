package pixelj.views.projectwindow.glyphspage;

import java.awt.Dialog;
import java.awt.Frame;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;
import pixelj.resources.Resources;
import pixelj.views.shared.Borders;
import pixelj.views.shared.Components;
import pixelj.views.shared.Dimensions;

abstract class AddCodePointDialogBase extends JDialog {

    private static final String INSETS = "insets 2lp";

    protected final JTextField cpIn = new JTextField();
    protected final JButton addButton = new JButton(Resources.get().getString("add"));
    protected final JButton closeButton = new JButton(Resources.get().getString("close"));
    protected final JRadioButton hex = new JRadioButton(Resources.get().getString("hexadecimal"));
    protected final JRadioButton dec = new JRadioButton(Resources.get().getString("decimal"));

    AddCodePointDialogBase(final Frame owner) {
        super(
                owner,
                Resources.get().getString("addCodePointDialogTitle"),
                Dialog.ModalityType.MODELESS
        );

        Components.setFixedSize(addButton, Dimensions.TEXT_BUTTON_SIZE);
        Components.setFixedSize(closeButton, Dimensions.TEXT_BUTTON_SIZE);

        final var pad = Dimensions.MEDIUM_PADDING;

        final var buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.add(Box.createHorizontalGlue());
        buttonPanel.add(addButton);
        buttonPanel.add(Box.createHorizontalStrut(Dimensions.MEDIUM_PADDING));
        buttonPanel.add(closeButton);

        final var content = new JPanel(new MigLayout(
                INSETS,
                String.format("[25%%]%dlp[25%%]%dlp[25%%]%dlp[25%%]", pad, pad, pad),
                String.format(
                        "[center, 24lp]%dlp[center, 24lp]%dlp[]",
                        pad, pad * 2
                )
        ));
        content.setBorder(Borders.LARGE_EMPTY);
        content.add(dec, "span 2");
        content.add(hex, "span 2, wrap");
        content.add(new JLabel(Resources.get().getString("codePoint")));
        content.add(cpIn, "span 3, growx, wrap");
        content.add(buttonPanel, "span 4, growx");

        setContentPane(content);
        pack();
        setResizable(false);
        setAlwaysOnTop(true);
    }

    /** When overridden, super.setVisible should be called last. */
    @Override
    public void setVisible(final boolean visible) {
        if (visible) {
            setLocationRelativeTo(getOwner());
            cpIn.requestFocusInWindow();
        }
        super.setVisible(visible);
    }
}
