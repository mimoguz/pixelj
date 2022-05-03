package io.github.mimoguz.pixelj.views.characters_screen;

import java.awt.Dimension;
import java.util.List;
import java.util.Objects;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

import com.formdev.flatlaf.FlatClientProperties;

import io.github.mimoguz.pixelj.actions.Actions;
import io.github.mimoguz.pixelj.actions.CharacterListActions;
import io.github.mimoguz.pixelj.controls.SearchableComboBox;
import io.github.mimoguz.pixelj.models.CharacterListModel;
import io.github.mimoguz.pixelj.models.CharacterModel;
import io.github.mimoguz.pixelj.models.Metrics;
import io.github.mimoguz.pixelj.resources.Resources;
import io.github.mimoguz.pixelj.util.Detachable;
import io.github.mimoguz.pixelj.views.shared.Borders;
import io.github.mimoguz.pixelj.views.shared.Components;
import io.github.mimoguz.pixelj.views.shared.Dimensions;

public class ListPanel extends JPanel implements Detachable {
    private static final long serialVersionUID = 7598472980158047622L;

    private final transient CharacterListActions actions;
    private final JButton addButton;
    private final SearchableComboBox<String> filterBox;
    private final JList<CharacterModel> list;
    private final JButton removeButton;
    private final transient ListSelectionModel selectionModel;

    public ListPanel(
            final CharacterListModel listModel,
            final ListSelectionModel selectionModel,
            final Metrics metrics,
            final JComponent root
    ) {
        this.selectionModel = selectionModel;

        final var res = Resources.get();

        actions = new CharacterListActions(listModel, selectionModel, metrics);
        actions.showRemoveDialogAction.setEnabled(false);
        Actions.registerShortcuts(actions.all, root);

        addButton = new JButton();
        addButton.setAction(actions.showAddDialogAction);
        Components.setFixedSize(addButton, Dimensions.TEXT_BUTTON_SIZE);

        removeButton = new JButton();
        removeButton.setAction(actions.showRemoveDialogAction);
        Components.setFixedSize(removeButton, Dimensions.TEXT_BUTTON_SIZE);

        list = new JList<>(listModel);
        list.setSelectionModel(selectionModel);
        list.setCellRenderer(new CharacterCellRenderer(48));
        list.setMaximumSize(Dimensions.MAXIMUM);
        setBorder(Borders.empty);

        filterBox = new SearchableComboBox<>(List.of(res.getString("showAll"), "60-70", "71-80", "81-90"));
        filterBox.setMaximumSize(Dimensions.MAXIMUM_COMBO_BOX_SIZE);
        filterBox.setMinimumSize(Dimensions.MINIMUM_COMBO_BOX_SIZE);
        filterBox.addActionListener(event -> {
            if (list.getModel() instanceof CharacterListModel lm) {
                final var item = filterBox.getSelectedItem();
                final var split = Objects.toString(item).split("-");
                try {
                    final var min = Integer.parseInt(split[0]);
                    final var max = Integer.parseInt(split[1]);
                    lm.setRange(min, max);
                } catch (Exception e) {
                    lm.setRange(0, Integer.MAX_VALUE);
                }
            }
        });

        setPreferredSize(new Dimension(300, 100));
        setMinimumSize(new Dimension(220, 100));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createMatteBorder(0, 1, 0, 0, res.colors.divider()));

        final var buttonPanel = new JPanel();
        buttonPanel.setBorder(Borders.smallEmpty);
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.add(Box.createHorizontalGlue());
        buttonPanel.add(addButton);
        buttonPanel.add(Box.createRigidArea(Dimensions.MEDIUM_SQUARE));
        buttonPanel.add(removeButton);
        buttonPanel.add(Box.createHorizontalGlue());
        add(buttonPanel);

        final var filterPanel = new JPanel();
        filterPanel.setBorder(Borders.smallEmptyCup);
        filterPanel.setLayout(new BoxLayout(filterPanel, BoxLayout.X_AXIS));
        filterPanel.add(filterBox);
        add(filterPanel);

        final var scrollPanel = new JScrollPane(list);
        scrollPanel.setMaximumSize(Dimensions.MAXIMUM);
        scrollPanel.setBorder(Borders.empty);
        add(scrollPanel);
    }

    @Override
    public void detach() {
        list.setModel(null);
    }

    public CharacterListActions getActions() {
        return actions;
    }

    public JButton getAddButton() {
        return addButton;
    }

    public SearchableComboBox<String> getFilterBox() {
        return filterBox;
    }

    public JList<CharacterModel> getList() {
        return list;
    }

    public JButton getRemoveButton() {
        return removeButton;
    }

    @Override
    public void setEnabled(final boolean value) {
        super.setEnabled(value);
        actions.showAddDialogAction.setEnabled(value);
        actions.showRemoveDialogAction.setEnabled(value && (selectionModel.getMinSelectionIndex() >= 0));
    }
}
