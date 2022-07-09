package pixelj.views.kerning_pairs_screen;

import java.awt.Frame;

import javax.swing.DefaultListSelectionModel;
import javax.swing.ListSelectionModel;

import pixelj.models.BlockRecord;
import pixelj.models.FilteredList;
import pixelj.models.Glyph;
import pixelj.models.KerningPair;
import pixelj.models.SortedList;
import pixelj.views.shared.Dimensions;
import pixelj.views.shared.GlyphCellRenderer;

public final class AddDialog extends AddDialogBase {

    private final ListSelectionModel selectionModel = new DefaultListSelectionModel();
    private Glyph left;
    private Glyph right;
    private KerningPair result;

    public AddDialog(final SortedList<Glyph> source, final Frame owner) {
        super(owner);

        final var listModel = new FilteredList<>(source);
        glyphList.setModel(listModel);

        selectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        glyphList.setSelectionModel(selectionModel);
        glyphList.setCellRenderer(new GlyphCellRenderer(Dimensions.MAXIMUM_PREVIEW_SIZE));

        filterBox.setSelectedIndex(0);
        filterBox.addActionListener(event -> {
            if (filterBox.getSelectedItem() instanceof final BlockRecord block) {
                listModel.setFilter(
                        chr -> chr.getCodePoint() >= block.starts() && chr.getCodePoint() <= block.ends()
                );
            }
        });

        setLeftButton.setEnabled(false);
        setLeftButton.addActionListener(event -> {
            if (selectionModel.getMinSelectionIndex() >= 0) {
                left = listModel.getElementAt(selectionModel.getMinSelectionIndex());
                leftCell.set(left, Integer.MAX_VALUE);
                addButton.setEnabled(right != null);
            }
        });

        setRightButton.setEnabled(false);
        setRightButton.addActionListener(event -> {
            if (selectionModel.getMinSelectionIndex() >= 0) {
                right = listModel.getElementAt(selectionModel.getMinSelectionIndex());
                rightCell.set(right, Integer.MAX_VALUE);
                addButton.setEnabled(right != null);
            }
        });

        selectionModel.addListSelectionListener(e -> {
            setLeftButton.setEnabled(selectionModel.getMinSelectionIndex() >= 0);
            setRightButton.setEnabled(selectionModel.getMinSelectionIndex() >= 0);
        });

        addButton.setEnabled(false);
        addButton.addActionListener(event -> {
            if (left != null && right != null) {
                result = new KerningPair(left, right, 0);
            }
            setVisible(false);
        });

        cancelButton.addActionListener(event -> setVisible(false));
    }

    public KerningPair getResult() {
        return result;
    }

    @Override
    public void setVisible(final boolean visible) {
        if (visible) {
            result = null;
            selectionModel.clearSelection();
        }
        super.setVisible(visible);
    }
}
