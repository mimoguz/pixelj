package io.github.pixelj.views.charactersScreen;

import com.formdev.flatlaf.FlatClientProperties;
import io.github.pixelj.actions.Actions;
import io.github.pixelj.actions.CharacterListActions;
import io.github.pixelj.controls.SearchableComboBox;
import io.github.pixelj.models.CharacterListModel;
import io.github.pixelj.models.CharacterModel;
import io.github.pixelj.resources.Resources;
import io.github.pixelj.views.shared.Borders;
import io.github.pixelj.views.shared.Components;
import io.github.pixelj.views.shared.Dimensions;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Objects;

public class CharacterListPanel extends JPanel {
    private final CharacterListActions actions;
    private final JButton addButton;
    private final SearchableComboBox<String> filterBox;
    private final JList<CharacterModel> list;
    private final JButton removeButton;
    private final ListSelectionModel selectionModel;

    public CharacterListPanel(
            @NotNull CharacterListModel listModel,
            @NotNull ListSelectionModel selectionModel,
            @NotNull JComponent root
    ) {
        this.selectionModel = selectionModel;

        final var res = Resources.get();

        actions = new CharacterListActions(listModel, selectionModel);
        actions.showRemoveDialogAction.setEnabled(false);
        Actions.registerShortcuts(actions.all, root);

        addButton = new JButton();
        addButton.setAction(actions.showAddDialogAction);
        Components.setFixedSize(addButton, Dimensions.textButtonSize);
        addButton.putClientProperty(FlatClientProperties.STYLE_CLASS, "textButton");

        removeButton = new JButton();
        removeButton.setAction(actions.showRemoveDialogAction);
        Components.setFixedSize(removeButton, Dimensions.textButtonSize);
        removeButton.putClientProperty(FlatClientProperties.STYLE_CLASS, "textButton");

        list = new JList<>(listModel);
        list.setSelectionModel(selectionModel);
        list.setCellRenderer(new CharacterCellRenderer(48));
        list.setMaximumSize(Dimensions.maximum);
        setBorder(Borders.empty);
        list.putClientProperty(FlatClientProperties.STYLE_CLASS, "focusList");

        filterBox = new SearchableComboBox<>(List.of(res.getString("showAll"), "60-70", "71-80", "81-90"));
        filterBox.setMaximumSize(Dimensions.maximumComboBoxSize);
        filterBox.setMinimumSize(Dimensions.minimumComboBoxSize);
        filterBox.addActionListener(event -> {
            final var item = filterBox.getSelectedItem();
            final var split = Objects.toString(item).split("-");
            try {
                final var min = Integer.parseInt(split[0]);
                final var max = Integer.parseInt(split[1]);
                listModel.setRange(min, max);
            } catch (Exception e) {
                listModel.setRange(0, Integer.MAX_VALUE);
            }
        });

        setPreferredSize(new Dimension(300, 100));
        setMinimumSize(new Dimension(220, 100));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createMatteBorder(0, 1, 0, 0, res.colors.divider()));

        final var buttonPanel = new JPanel();
        buttonPanel.setBorder(Borders.empty4);
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.add(Box.createHorizontalGlue());
        buttonPanel.add(addButton);
        buttonPanel.add(Box.createRigidArea(Dimensions.square8));
        buttonPanel.add(removeButton);
        buttonPanel.add(Box.createHorizontalGlue());
        add(buttonPanel);

        // TODO: finish me!
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
    public void setEnabled(boolean value) {
        super.setEnabled(value);
        Actions.setEnabled(actions.all, value);
    }

    public void setListModel(CharacterListModel listModel) {
        selectionModel.clearSelection();
        list.setModel(listModel);
    }
}
