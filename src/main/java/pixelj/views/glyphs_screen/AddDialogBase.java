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
 * AddDialog design. This should be extended to add functionality.
 */
abstract class AddDialogBase extends JDialog {
    /**
     * List of scalars.
     */
    protected final JList<ScalarRecord> listView = new JList<>();
    /**
     * Block selection.
     */
    protected final JComboBox<BlockRecord> filterBox = new SearchableComboBox<>();
    /**
     * Positive action.
     */
    protected final JButton addButton;
    /**
     * Negative action. Default.
     */
    protected final JButton cancelButton;

    AddDialogBase(final Frame owner) {
        super(owner, Resources.get().getString("addGlyphsTitle"), Dialog.ModalityType.APPLICATION_MODAL);

        final var res = Resources.get();

        listView.setCellRenderer(new ScalarCellRenderer());
        
        final var scrollPanel = new JScrollPane(listView);
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
        listView.requestFocusInWindow();
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
            listView.requestFocusInWindow();
        }
        super.setVisible(visible);
    }
}
