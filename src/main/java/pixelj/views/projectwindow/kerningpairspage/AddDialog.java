package pixelj.views.projectwindow.kerningpairspage;

import java.awt.Frame;
import java.awt.event.KeyEvent;

import javax.swing.DefaultListSelectionModel;
import javax.swing.JComponent;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;

import pixelj.actions.ApplicationAction;
import pixelj.messaging.AddKerningPairMessage;
import pixelj.models.Block;
import pixelj.models.FilteredList;
import pixelj.models.Glyph;
import pixelj.models.KerningPair;
import pixelj.models.Project;
import pixelj.resources.Icon;
import pixelj.resources.Resources;
import pixelj.messaging.Messenger;
import pixelj.views.shared.Dimensions;
import pixelj.views.shared.GlyphCellRenderer;
import pixelj.views.shared.Help;

public final class AddDialog extends AddDialogBase {

    private final ListSelectionModel selectionModel = new DefaultListSelectionModel();
    private Glyph left;
    private Glyph right;

    public AddDialog(final Frame owner, final Project project) {
        super(owner);

        final var listModel = new FilteredList<>(project.getGlyphs());
        glyphList.setModel(listModel);

        selectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        glyphList.setSelectionModel(selectionModel);
        glyphList.setCellRenderer(new GlyphCellRenderer(Dimensions.MAXIMUM_PREVIEW_SIZE));

        filterBox.setSelectedIndex(0);
        filterBox.addActionListener(event -> {
            if (filterBox.getSelectedItem() instanceof final Block block) {
                listModel.setFilter(
                    chr -> chr.getCodePoint() >= block.starts() && chr.getCodePoint() <= block.ends()
                );
            }
        });

        setLeftButton.setEnabled(false);
        setLeftButton.addActionListener(event -> {
            if (selectionModel.getMinSelectionIndex() >= 0) {
                left = listModel.getElementAt(selectionModel.getMinSelectionIndex());
                leftCell.set(left);
                addButton.setEnabled(right != null && left != null);
            }
        });

        setRightButton.setEnabled(false);
        setRightButton.addActionListener(event -> {
            if (selectionModel.getMinSelectionIndex() >= 0) {
                right = listModel.getElementAt(selectionModel.getMinSelectionIndex());
                rightCell.set(right);
                addButton.setEnabled(right != null && left != null);
            }
        });

        helpButton.setAction(new ApplicationAction(
                "addKerningPairHelpAction",
                (e, action) -> Help.showPage(Help.Page.KERNING_PAIRS)
            )
                .setIcon(Icon.HELP)
                .setTooltip(Resources.get().getString("help"))
        );

        selectionModel.addListSelectionListener(e -> {
            setLeftButton.setEnabled(selectionModel.getMinSelectionIndex() >= 0);
            setRightButton.setEnabled(selectionModel.getMinSelectionIndex() >= 0);
        });

        // TODO: Separate events
        addButton.addActionListener(event -> addKerningPair());
        addButton.setEnabled(false);

        cancelButton.addActionListener(event -> setVisible(false));

        getRootPane().registerKeyboardAction(
            (e) -> setVisible(false),
            KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
            JComponent.WHEN_IN_FOCUSED_WINDOW
        );
    }

    @Override
    public void setVisible(final boolean visible) {
        if (visible) {
            selectionModel.clearSelection();
        }
        super.setVisible(visible);
    }

    private void addKerningPair() {
        if (left != null && right != null) {
            Messenger.get(AddKerningPairMessage.class).send(new AddKerningPairMessage(left, right, mirroredCheck.isSelected()));
        }
    }
}
