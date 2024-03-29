package pixelj.views.projectwindow.glyphspage;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.Frame;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.formdev.flatlaf.FlatClientProperties;

import net.miginfocom.swing.MigLayout;
import pixelj.models.Block;
import pixelj.models.Scalar;
import pixelj.resources.Resources;
import pixelj.views.controls.SearchableComboBox;
import pixelj.views.shared.Borders;
import pixelj.views.shared.Components;
import pixelj.views.shared.Dimensions;
import pixelj.views.shared.ScalarCellRenderer;

/** AddDialog design. */
abstract class AddDialogBase extends JDialog {

    private static final String INSETS = "insets 2lp";

    protected final JList<Scalar> scalarList = new JList<>();
    /**  Filter scalars by block. */
    protected final JComboBox<Block> filterBox = new SearchableComboBox<>(
            Resources.get().getBlocks().stream().skip(1).toList()
    );
    protected final JButton addButton = new JButton(Resources.get().getString("add"));
    protected final JButton closeButton = new JButton(Resources.get().getString("close"));
    protected final JButton helpButton = new JButton();

    AddDialogBase(final Frame owner) {
        super(
                owner,
                Resources.get().getString("addGlyphsDialogTitle"),
                Dialog.ModalityType.MODELESS
        );

        scalarList.setCellRenderer(new ScalarCellRenderer());

        final var scrollPanel = new JScrollPane(scalarList);
        scrollPanel.setMaximumSize(Dimensions.MAXIMUM);

        Components.setFixedSize(addButton, Dimensions.TEXT_BUTTON_SIZE);
        Components.setFixedSize(closeButton, Dimensions.TEXT_BUTTON_SIZE);

        helpButton.putClientProperty(
                FlatClientProperties.BUTTON_TYPE,
                FlatClientProperties.BUTTON_TYPE_BORDERLESS
        );

        final var pad = Dimensions.MEDIUM_PADDING;
        final var buttonPanel = new JPanel(
                new MigLayout(
                    INSETS,
                    String.format("[grow, left]%dlp[]%dlp[]", pad, pad),
                    "[center]"
                )
        );
        buttonPanel.add(helpButton);
        buttonPanel.add(addButton);
        buttonPanel.add(closeButton);

        final var content = new JPanel();
        content.setLayout(new BorderLayout(Dimensions.MEDIUM_PADDING, Dimensions.MEDIUM_PADDING));

        content.add(filterBox, BorderLayout.NORTH);
        content.add(scrollPanel, BorderLayout.CENTER);
        content.add(buttonPanel, BorderLayout.SOUTH);
        content.setBorder(Borders.MEDIUM_EMPTY);

        setContentPane(content);
        getRootPane().setDefaultButton(closeButton);
        pack();
        scalarList.requestFocusInWindow();
        setSize(400, 600);
        setResizable(true);
        setAlwaysOnTop(true);
    }

    /** When overridden, super.setVisible should be called last. */
    @Override
    public void setVisible(final boolean visible) {
        if (visible) {
            setLocationRelativeTo(getOwner());
            scalarList.requestFocusInWindow();
        }
        super.setVisible(visible);
    }
}
