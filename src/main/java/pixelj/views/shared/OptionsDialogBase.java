package pixelj.views.shared;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Frame;
import java.awt.Dialog;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.formdev.flatlaf.FlatClientProperties;
import java.awt.Dimension;

import pixelj.resources.Resources;
import pixelj.services.AppState;

public abstract class OptionsDialogBase extends JDialog {

    protected final JComboBox<AppState.ColorTheme> colorThemeIn = new JComboBox<>();
    protected final JComboBox<AppState.IconTheme> iconThemeIn = new JComboBox<>();
    protected final JButton saveButton = new JButton(Resources.get().getString("save"));
    protected final JButton cancelButton = new JButton(Resources.get().getString("cancel"));

    OptionsDialogBase(final Frame owner) {
        super(owner, Resources.get().getString("optionsDialogTitle"), Dialog.ModalityType.APPLICATION_MODAL);

        final var content = new JPanel(new GridBagLayout());
        content.setBorder(Borders.LARGE_EMPTY);

        final var res = Resources.get();
        final var requiresRestart = res.getString("requiresRestart");

        final var cons = new GridBagConstraints();
        cons.fill = GridBagConstraints.NONE;

        cons.insets = new Insets(0, 0, 0, Dimensions.LARGE_PADDING);
        cons.gridx = 0;
        cons.gridy = 0;
        cons.anchor = GridBagConstraints.WEST;
        content.add(new JLabel(res.getString("appTheme")), cons);

        cons.gridy = 1;
        final var footnote1 = new JLabel(res.getString("requiresRestart"));
        footnote1.putClientProperty(FlatClientProperties.STYLE_CLASS, "small");
        content.add(footnote1, cons);
        
        cons.gridy = 2;
        cons.insets = new Insets(0, 0, 0, 0);
        content.add(Box.createRigidArea(Dimensions.LARGE_SQUARE), cons);
        
        cons.insets = new Insets(0, 0, 0, Dimensions.LARGE_PADDING);
        cons.gridy = 3;
        cons.anchor = GridBagConstraints.WEST;
        content.add(new JLabel(res.getString("iconTheme")), cons);

        cons.gridy = 4;
        final var footnote2 = new JLabel(requiresRestart);
        footnote2.putClientProperty(FlatClientProperties.STYLE_CLASS, "small");
        content.add(footnote2, cons);

        cons.gridx = 1;
        cons.gridy = 0;
        cons.gridheight = 2;
        cons.insets = new Insets(0, Dimensions.LARGE_PADDING, 0, 0);
        cons.anchor = GridBagConstraints.EAST;
        content.add(colorThemeIn, cons);
        
        cons.gridy = 3;
        cons.gridheight = 2;
        content.add(iconThemeIn, cons);

        cons.gridx = 0;
        cons.gridy = 5;
        cons.gridwidth = 2;
        cons.gridheight = 1;
        cons.insets = new Insets(Dimensions.LARGE_PADDING * 2, 0, 0, 0);
        cons.anchor = GridBagConstraints.EAST;
        final var buttonBox = new JPanel();
        buttonBox.setLayout(new BoxLayout(buttonBox, BoxLayout.X_AXIS));
        buttonBox.add(saveButton);
        buttonBox.add(Box.createRigidArea(Dimensions.MEDIUM_SQUARE));
        buttonBox.add(cancelButton);
        content.add(buttonBox, cons);

        setContentPane(content);
        getRootPane().setDefaultButton(saveButton);
        pack();
        setSize(new Dimension(232, 192));
        setResizable(false);
    }

    /**
     * @see java.awt.Dialog#setVisible(boolean)
     */
    @Override
    public void setVisible(final boolean visible) {
        if (visible) {
            setLocationRelativeTo(getOwner());
            colorThemeIn.requestFocusInWindow();
        }
        super.setVisible(visible);
    }
}
