package pixelj.views.glyphs_screen;

import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

import pixelj.actions.Actions;
import pixelj.actions.GlyphListActions;
import pixelj.models.BlockRecord;
import pixelj.models.Glyph;
import pixelj.models.FilteredList;
import pixelj.models.DocumentSettings;
import pixelj.models.Project;
import pixelj.models.SortedList;
import pixelj.resources.Resources;
import pixelj.util.Detachable;
import pixelj.views.controls.SearchableComboBox;
import pixelj.views.shared.Borders;
import pixelj.views.shared.GlyphCellRenderer;
import pixelj.views.shared.Components;
import pixelj.views.shared.Dimensions;

public final class ListPanel extends JPanel implements Detachable {
    private final GlyphListActions actions;
    private final SearchableComboBox<BlockRecord> filterBox = new SearchableComboBox<>(
            Resources.get().getBlocks()
    );
    private final JList<Glyph> list;
    private final SortedList<Glyph> listModel;
    private final ListSelectionModel selectionModel;

    public ListPanel(
            final Project project,
            final ListSelectionModel selectionModel,
            final DocumentSettings metrics,
            final JComponent root
    ) {
        this.selectionModel = selectionModel;

        final var res = Resources.get();

        actions = new GlyphListActions(project, selectionModel, root);
        actions.removeGlyphsAction.setEnabled(false);
        Actions.registerShortcuts(actions.all, root);

        final var addButton = new JButton();
        addButton.setAction(actions.addGlyphsAction);
        Components.setFixedSize(addButton, Dimensions.TEXT_BUTTON_SIZE);

        final var removeButton = new JButton();
        removeButton.setAction(actions.removeGlyphsAction);
        Components.setFixedSize(removeButton, Dimensions.TEXT_BUTTON_SIZE);

        final var filteredListModel = new FilteredList<>(project.getGlyphs());
        list = new JList<>(filteredListModel);
        list.setSelectionModel(selectionModel);
        list.setCellRenderer(new GlyphCellRenderer(48));
        list.setMaximumSize(Dimensions.MAXIMUM);
        setBorder(Borders.EMPTY);

        filterBox.setMaximumSize(Dimensions.MAXIMUM_COMBO_BOX_SIZE);
        filterBox.setMinimumSize(Dimensions.MINIMUM_COMBO_BOX_SIZE);
        filterBox.addActionListener(event -> {
            if (filterBox.getSelectedItem() instanceof final BlockRecord block) {
                filteredListModel.setFilter(
                        chr -> chr.getCodePoint() >= block.starts() && chr.getCodePoint() <= block.ends()
                );
            } else {
                filteredListModel.setFilter(chr -> true);
            }
        });

        this.listModel = filteredListModel;

        setPreferredSize(new Dimension(300, 100));
        setMinimumSize(new Dimension(220, 100));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createMatteBorder(0, 1, 0, 0, res.colors.divider()));

        final var buttonPanel = new JPanel();
        buttonPanel.setBorder(Borders.SMALL_EMPTY);
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.add(Box.createHorizontalGlue());
        buttonPanel.add(addButton);
        buttonPanel.add(Box.createRigidArea(Dimensions.MEDIUM_SQUARE));
        buttonPanel.add(removeButton);
        buttonPanel.add(Box.createHorizontalGlue());
        add(buttonPanel);

        final var filterPanel = new JPanel();
        filterPanel.setBorder(Borders.SMALL_EMPTY_CUP);
        filterPanel.setLayout(new BoxLayout(filterPanel, BoxLayout.X_AXIS));
        filterPanel.add(filterBox);
        add(filterPanel);

        final var scrollPanel = new JScrollPane(list);
        scrollPanel.setMaximumSize(Dimensions.MAXIMUM);
        scrollPanel.setBorder(Borders.EMPTY);
        add(scrollPanel);
    }

    @Override
    public void detach() {
        list.setModel(null);
    }

    public GlyphListActions getActions() {
        return actions;
    }

    public JList<Glyph> getList() {
        return list;
    }

    public SortedList<Glyph> getListModel() {
        return listModel;
    }

    @Override
    public void setEnabled(final boolean value) {
        super.setEnabled(value);
        actions.addGlyphsAction.setEnabled(value);
        actions.removeGlyphsAction.setEnabled(value && (selectionModel.getMinSelectionIndex() >= 0));
    }
}
