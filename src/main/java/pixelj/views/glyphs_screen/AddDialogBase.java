package pixelj.views.glyphs_screen;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.Frame;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import pixelj.models.BlockRecord;
import pixelj.models.ScalarRecord;
import pixelj.resources.Resources;
import pixelj.views.controls.SearchableComboBox;
import pixelj.views.shared.Borders;
import pixelj.views.shared.Components;
import pixelj.views.shared.Dimensions;
import pixelj.views.shared.ScalarCellRenderer;

/**
 * AddDialog design.
 */
abstract class AddDialogBase extends JDialog {
    protected final JList<ScalarRecord> scalarList = new JList<>();
    /**
     * Filter scalars by block.
     */
    protected final JComboBox<BlockRecord> filterBox = new SearchableComboBox<>(
            Resources.get().getBlocks().stream().skip(1).toList()
    );
    protected final JButton addButton;
    protected final JButton cancelButton;

    AddDialogBase(final Frame owner) {
        super(owner, Resources.get().getString("addGlyphsTitle"), Dialog.ModalityType.APPLICATION_MODAL);

        final var res = Resources.get();

        scalarList.setCellRenderer(new ScalarCellRenderer());
        
        final var scrollPanel = new JScrollPane(scalarList);
        scrollPanel.setMaximumSize(Dimensions.MAXIMUM);
        
        addButton = new JButton(res.getString("add"));
        Components.setFixedSize(addButton, Dimensions.TEXT_BUTTON_SIZE);

        cancelButton = new JButton(res.getString("cancel"));
        Components.setFixedSize(cancelButton, Dimensions.TEXT_BUTTON_SIZE);

        final var buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.add(Box.createHorizontalGlue());
        buttonPanel.add(addButton);
        buttonPanel.add(Box.createRigidArea(Dimensions.MEDIUM_SQUARE));
        buttonPanel.add(cancelButton);

        final var content = new JPanel();
        content.setLayout(new BorderLayout(Dimensions.MEDIUM_PADDING, Dimensions.MEDIUM_PADDING));

        content.add(filterBox, BorderLayout.NORTH);
        content.add(scrollPanel, BorderLayout.CENTER);
        content.add(buttonPanel, BorderLayout.SOUTH);
        content.setBorder(Borders.MEDIUM_EMPTY);
        setContentPane(content);
        getRootPane().setDefaultButton(cancelButton);

        pack();
        scalarList.requestFocusInWindow();
        setSize(400, 600);
        setResizable(true);
    }

    /**
     * When overridden, super.setVisible should be called last.
     * */
    @Override
    public void setVisible(final boolean visible) {
        if (visible) {
            setLocationRelativeTo(getOwner());
            scalarList.requestFocusInWindow();
        }
        super.setVisible(visible);
    }
}
