package pixelj.views.characters_screen;

import pixelj.actions.Actions;
import pixelj.actions.CharacterListActions;
import pixelj.models.*;
import pixelj.resources.Resources;
import pixelj.util.Detachable;
import pixelj.views.controls.SearchableComboBox;
import pixelj.views.shared.Borders;
import pixelj.views.shared.CharacterCellRenderer;
import pixelj.views.shared.Components;
import pixelj.views.shared.Dimensions;

import javax.swing.*;
import java.awt.*;

public class ListPanel extends JPanel implements Detachable {
    private final CharacterListActions actions;
    private final SearchableComboBox<BlockData> filterBox = new SearchableComboBox<>(Resources.get().getBlocks());
    private final JList<CharacterItem> list;
    private final SortedList<CharacterItem> listModel;
    private final ListSelectionModel selectionModel;

    public ListPanel(
            final Project project,
            final ListSelectionModel selectionModel,
            final Metrics metrics,
            final JComponent root
    ) {
        this.selectionModel = selectionModel;

        final var res = Resources.get();

        actions = new CharacterListActions(project, selectionModel, metrics, root);
        actions.showRemoveDialogAction.setEnabled(false);
        Actions.registerShortcuts(actions.all, root);

        final var addButton = new JButton();
        addButton.setAction(actions.showAddDialogAction);
        Components.setFixedSize(addButton, Dimensions.TEXT_BUTTON_SIZE);

        final var removeButton = new JButton();
        removeButton.setAction(actions.showRemoveDialogAction);
        Components.setFixedSize(removeButton, Dimensions.TEXT_BUTTON_SIZE);

        final var listModel = new FilteredList<>(project.getCharacters());
        list = new JList<>(listModel);
        list.setSelectionModel(selectionModel);
        list.setCellRenderer(new CharacterCellRenderer(48));
        list.setMaximumSize(Dimensions.MAXIMUM);
        setBorder(Borders.EMPTY);

        filterBox.setMaximumSize(Dimensions.MAXIMUM_COMBO_BOX_SIZE);
        filterBox.setMinimumSize(Dimensions.MINIMUM_COMBO_BOX_SIZE);
        filterBox.addActionListener(event -> {
            if (filterBox.getSelectedItem() instanceof BlockData block) {
                listModel.setFilter(
                        chr -> chr.getCodePoint() >= block.starts() && chr.getCodePoint() <= block.ends()
                );
            } else {
                listModel.setFilter(chr -> true);
            }
        });

        this.listModel = listModel;

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

    public CharacterListActions getActions() {
        return actions;
    }

    public JList<CharacterItem> getList() {
        return list;
    }

    public SortedList<CharacterItem> getListModel() {
        return listModel;
    }

    @Override
    public void setEnabled(final boolean value) {
        super.setEnabled(value);
        actions.showAddDialogAction.setEnabled(value);
        actions.showRemoveDialogAction.setEnabled(value && (selectionModel.getMinSelectionIndex() >= 0));
    }
}
