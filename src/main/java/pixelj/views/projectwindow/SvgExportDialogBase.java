package pixelj.views.projectwindow;

import com.formdev.flatlaf.FlatClientProperties;
import net.miginfocom.swing.MigLayout;
import pixelj.resources.Resources;
import pixelj.views.shared.Borders;
import pixelj.views.shared.Components;
import pixelj.views.shared.Dimensions;

import javax.swing.*;
import java.awt.*;

public class SvgExportDialogBase extends JDialog {

    protected final JButton exportButton = new JButton(Resources.get().getString("export"));
    protected final JButton cancelButton = new JButton(Resources.get().getString("cancel"));
    protected final JTextField pathField = new JTextField();
    protected final JCheckBox genScriptCheckBox = new JCheckBox(Resources.get().getString("genFontForgeScript"));
    protected final JButton selectPathButton = new JButton(Resources.get().getString("selectOutDirectory"));
    protected final JButton helpButton = new JButton();

    public SvgExportDialogBase(final Frame owner) {
        super(owner, Resources.get().getString("svgExportDialogTitle"), Dialog.ModalityType.APPLICATION_MODAL);

        Components.setFixedSize(exportButton, Dimensions.TEXT_BUTTON_SIZE);
        Components.setFixedSize(cancelButton, Dimensions.TEXT_BUTTON_SIZE);
        Components.setFixedSize(selectPathButton, Dimensions.TEXT_BUTTON_SIZE);
        helpButton.putClientProperty(
            FlatClientProperties.BUTTON_TYPE,
            FlatClientProperties.BUTTON_TYPE_BORDERLESS
        );

        final var content = new JPanel(new BorderLayout());
        final var inputPanel = new JPanel(new MigLayout(
            "insets 2lp",
            "[left, min!]" + Dimensions.LARGE_PADDING + "lp[left, grow]" + Dimensions.MEDIUM_PADDING + "lp[left, min!]",
            "[center, min!]" + Dimensions.LARGE_PADDING + "lp[center, min!]"
        ));
        inputPanel.setBorder(Borders.LARGE_EMPTY);
        inputPanel.add(new JLabel(Resources.get().getString("outDirectory")));
        inputPanel.add(pathField, "grow");
        inputPanel.add(selectPathButton, "wrap");
        inputPanel.add(genScriptCheckBox, "span, right");

        content.add(inputPanel, BorderLayout.CENTER);

        final var buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.setBorder(Borders.LARGE_EMPTY);
        buttonPanel.add(helpButton);
        buttonPanel.add(Box.createHorizontalGlue());
        buttonPanel.add(exportButton);
        buttonPanel.add(Box.createRigidArea(Dimensions.MEDIUM_SQUARE));
        buttonPanel.add(cancelButton);
        content.add(buttonPanel, BorderLayout.SOUTH);

        setContentPane(content);
        getRootPane().setDefaultButton(exportButton);
        pack();
        setSize(640, 172);
        setResizable(false);
        setLocationRelativeTo(owner);
    }

    @Override
    public void setVisible(final boolean visible) {
        if (visible) {
            setLocationRelativeTo(getOwner());
        }
        super.setVisible(visible);
    }
}
