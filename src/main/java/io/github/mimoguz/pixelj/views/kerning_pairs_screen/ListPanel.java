package io.github.mimoguz.pixelj.views.kerning_pairs_screen;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.function.BiConsumer;
import java.util.function.Function;

import javax.swing.*;

import com.formdev.flatlaf.FlatClientProperties;

import io.github.mimoguz.pixelj.actions.Actions;
import io.github.mimoguz.pixelj.actions.KerningPairListActions;
import io.github.mimoguz.pixelj.controls.SearchableComboBox;
import io.github.mimoguz.pixelj.models.BlockModel;
import io.github.mimoguz.pixelj.models.KerningPairListModel;
import io.github.mimoguz.pixelj.models.KerningPairModel;
import io.github.mimoguz.pixelj.resources.Resources;
import io.github.mimoguz.pixelj.util.Detachable;
import io.github.mimoguz.pixelj.views.shared.Borders;
import io.github.mimoguz.pixelj.views.shared.Components;
import io.github.mimoguz.pixelj.views.shared.Dimensions;

public class ListPanel extends JPanel implements Detachable {
    private static final long serialVersionUID = -3269592942325114705L;

    private final transient KerningPairListActions actions;
    private final JButton addButton;
    private final SearchableComboBox<BlockModel> leftFilterBox;
    private final JList<KerningPairModel> list;
    private final JButton removeButton;
    private final SearchableComboBox<BlockModel> rightFilterBox;
    private final transient ListSelectionModel selectionModel;

    public ListPanel(
            final KerningPairListModel listModel,
            final ListSelectionModel selectionModel,
            final JComponent root
    ) {
        this.selectionModel = selectionModel;

        final var res = Resources.get();

        actions = new KerningPairListActions(listModel, selectionModel);
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
        list.setCellRenderer(new KerningPairCellRenderer(48));
        list.setMaximumSize(Dimensions.MAXIMUM);
        setBorder(Borders.empty);

        leftFilterBox = filterBox(lm -> (lm::setLeftRange));
        rightFilterBox = filterBox(lm -> (lm::setRightRange));

        setPreferredSize(new Dimension(400, 100));
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
        filterPanel.setLayout(new GridLayout(2, 2, 4, 0));
        final var leftTitle = new JLabel(res.getString("leftTitle"));
        leftTitle.putClientProperty(FlatClientProperties.STYLE_CLASS, "h4");
        leftTitle.setHorizontalAlignment(SwingConstants.CENTER);
        filterPanel.add(leftTitle);
        final var rightTitle = new JLabel(res.getString("rightTitle"));
        rightTitle.putClientProperty(FlatClientProperties.STYLE_CLASS, "h4");
        rightTitle.setHorizontalAlignment(SwingConstants.CENTER);
        filterPanel.add(rightTitle);
        filterPanel.add(leftFilterBox);
        filterPanel.add(rightFilterBox);
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

    public KerningPairListActions getActions() {
        return actions;
    }

    public JButton getAddButton() {
        return addButton;
    }

    public SearchableComboBox<BlockModel> getLeftFilterBox() {
        return leftFilterBox;
    }

    public JList<KerningPairModel> getList() {
        return list;
    }

    public JButton getRemoveButton() {
        return removeButton;
    }

    public SearchableComboBox<BlockModel> getRightFilterBox() {
        return rightFilterBox;
    }

    @Override
    public void setEnabled(boolean value) {
        super.setEnabled(value);
        actions.showAddDialogAction.setEnabled(value);
        actions.showRemoveDialogAction.setEnabled(value && (selectionModel.getMinSelectionIndex() >= 0));
    }

    private SearchableComboBox<BlockModel> filterBox(
            Function<KerningPairListModel, BiConsumer<Integer, Integer>> setter
    ) {
        final var box = new SearchableComboBox<BlockModel>(Resources.get().blockList);
        box.setMaximumSize(Dimensions.MAXIMUM_COMBO_BOX_SIZE);
        box.setMinimumSize(Dimensions.MINIMUM_COMBO_BOX_SIZE);
        box.addActionListener(event -> {
            if (list.getModel() instanceof KerningPairListModel lm) {
                final var item = box.getSelectedItem();
                try {
                    final var block = (BlockModel) item;
                    setter.apply(lm).accept(block.starts(), block.ends());
                } catch (Exception e) {
                    setter.apply(lm).accept(0, Integer.MAX_VALUE);
                }
            }
        });
        return box;
    }
}
