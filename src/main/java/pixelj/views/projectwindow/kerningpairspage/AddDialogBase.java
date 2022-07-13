package pixelj.views.projectwindow.kerningpairspage;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.Frame;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import pixelj.models.BlockRecord;
import pixelj.models.Glyph;
import pixelj.resources.Resources;
import pixelj.views.controls.SearchableComboBox;
import pixelj.views.shared.Borders;
import pixelj.views.shared.Components;
import pixelj.views.shared.Dimensions;
import pixelj.views.shared.GlyphCell;
import pixelj.views.shared.GlyphCellRenderer;

/**
 * Add dialog design.
 */
public abstract class AddDialogBase extends JDialog {

    protected final JComboBox<BlockRecord> filterBox = new SearchableComboBox<>(Resources.get().getBlocks());
    protected final JList<Glyph> glyphList = new JList<>();
    protected final GlyphCell leftCell = new GlyphCell(Dimensions.MAXIMUM_PREVIEW_SIZE);
    protected final GlyphCell rightCell = new GlyphCell(Dimensions.MAXIMUM_PREVIEW_SIZE);
    protected final JButton setLeftButton = new JButton(Resources.get().getString("setLeft"));
    protected final JButton setRightButton = new JButton(Resources.get().getString("setRight"));
    protected final JButton addButton = new JButton(Resources.get().getString("add"));
    protected final JButton cancelButton = new JButton(Resources.get().getString("cancel"));

    AddDialogBase(final Frame owner) {
        super(
                owner,
                Resources.get().getString("kerningPairsAddDialogTitle"),
                Dialog.ModalityType.APPLICATION_MODAL
        );

        glyphList.setCellRenderer(new GlyphCellRenderer(Dimensions.MAXIMUM_PREVIEW_SIZE));
        Components.setFixedSize(addButton, Dimensions.TEXT_BUTTON_SIZE);
        Components.setFixedSize(setLeftButton, Dimensions.TEXT_BUTTON_SIZE);
        Components.setFixedSize(setRightButton, Dimensions.TEXT_BUTTON_SIZE);
        Components.setFixedSize(cancelButton, Dimensions.TEXT_BUTTON_SIZE);

        final var scrollPanel = new JScrollPane(glyphList);
        scrollPanel.setMaximumSize(Dimensions.MAXIMUM);

        final var centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(scrollPanel, BorderLayout.CENTER);

        final var selectionPanel = new JPanel();
        selectionPanel.setBorder(Borders.EMPTY);
        final var selectionPanelLayout = new GroupLayout(selectionPanel);
        selectionPanel.setLayout(selectionPanelLayout);

        selectionPanelLayout.setHorizontalGroup(
                selectionPanelLayout.createSequentialGroup()
                        .addGroup(
                                selectionPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(setLeftButton)
                                        .addComponent(setRightButton)
                        )
                        .addGroup(
                                selectionPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(
                                                leftCell,
                                                GroupLayout.DEFAULT_SIZE,
                                                GroupLayout.PREFERRED_SIZE,
                                                Short.MAX_VALUE
                                        )
                                        .addComponent(
                                                rightCell,
                                                GroupLayout.DEFAULT_SIZE,
                                                GroupLayout.PREFERRED_SIZE,
                                                Short.MAX_VALUE
                                        )
                        )
        );

        selectionPanelLayout.setVerticalGroup(
                selectionPanelLayout.createSequentialGroup()
                        .addGroup(
                                selectionPanelLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
                                        .addComponent(setLeftButton)
                                        .addComponent(leftCell, 64, 64, 64)
                        )
                        .addGroup(
                                selectionPanelLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
                                        .addComponent(setRightButton)
                                        .addComponent(rightCell, 64, 64, 64)
                        )
        );

        centerPanel.add(selectionPanel, BorderLayout.SOUTH);

        final var buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.add(Box.createHorizontalGlue());
        buttonPanel.add(addButton);
        buttonPanel.add(Box.createRigidArea(Dimensions.MEDIUM_SQUARE));
        buttonPanel.add(cancelButton);

        final var content = new JPanel();
        content.setLayout(new BorderLayout(Dimensions.MEDIUM_PADDING, Dimensions.MEDIUM_PADDING));

        content.add(filterBox, BorderLayout.NORTH);
        content.add(centerPanel, BorderLayout.CENTER);
        content.add(buttonPanel, BorderLayout.SOUTH);
        content.setBorder(Borders.MEDIUM_EMPTY);
        setContentPane(content);
        getRootPane().setDefaultButton(cancelButton);

        pack();
        glyphList.requestFocusInWindow();
        setSize(400, 600);
        setResizable(true);
    }

     /**
     *  @param visible Is visible. Note that since this dialog is modal, passing true here block the caller.
     */
    @Override
    public void setVisible(final boolean visible) {
        if (visible) {
            setLocationRelativeTo(getOwner());
            glyphList.requestFocusInWindow();
        }
        super.setVisible(visible);
    }
}
