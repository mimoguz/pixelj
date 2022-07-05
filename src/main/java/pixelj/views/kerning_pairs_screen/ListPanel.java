package pixelj.views.kerning_pairs_screen;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.function.BiConsumer;
import java.util.function.Predicate;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;

import com.formdev.flatlaf.FlatClientProperties;

import pixelj.actions.Actions;
import pixelj.actions.KerningPairListActions;
import pixelj.models.BlockRecord;
import pixelj.models.FilteredList;
import pixelj.models.KerningPair;
import pixelj.models.Project;
import pixelj.models.SortedList;
import pixelj.resources.Resources;
import pixelj.util.Detachable;
import pixelj.views.controls.SearchableComboBox;
import pixelj.views.shared.Borders;
import pixelj.views.shared.Components;
import pixelj.views.shared.Dimensions;

public final class ListPanel extends JPanel implements Detachable {
    private final KerningPairListActions actions;
    private final Predicate<KerningPair> filterRight = model -> true;
    private final JList<KerningPair> list;
    private final FilteredList<KerningPair> listModel;
    private final ListSelectionModel selectionModel;
    private Predicate<KerningPair> filterLeft = model -> true;

    public ListPanel(final Project project, final ListSelectionModel selectionModel, final JComponent root) {
        this.selectionModel = selectionModel;

        final var res = Resources.get();

        actions = new KerningPairListActions(project, selectionModel, root);
        actions.showRemoveDialogAction.setEnabled(false);
        Actions.registerShortcuts(actions.all, root);

        final var addButton = new JButton();
        addButton.setAction(actions.showAddDialogAction);
        Components.setFixedSize(addButton, Dimensions.TEXT_BUTTON_SIZE);

        final var removeButton = new JButton();
        removeButton.setAction(actions.showRemoveDialogAction);
        Components.setFixedSize(removeButton, Dimensions.TEXT_BUTTON_SIZE);

        listModel = new FilteredList<>(project.getKerningPairs());
        list = new JList<>(listModel);
        list.setSelectionModel(selectionModel);
        list.setCellRenderer(new KerningPairCellRenderer(48));
        list.setMaximumSize(Dimensions.MAXIMUM);
        setBorder(Borders.EMPTY);

        final var leftFilterBox = filterBox(this::setFilterLeft);
        final var rightFilterBox = filterBox(this::setFilterRight);

        setPreferredSize(new Dimension(520, 100));
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
        scrollPanel.setBorder(Borders.EMPTY);
        add(scrollPanel);
    }

    @Override
    public void detach() {
        list.setModel(null);
    }

    public KerningPairListActions getActions() {
        return actions;
    }

    public JList<KerningPair> getList() {
        return list;
    }

    public SortedList<KerningPair> getListModel() {
        return listModel;
    }

    @Override
    public void setEnabled(final boolean value) {
        super.setEnabled(value);
        actions.showAddDialogAction.setEnabled(value);
        actions.showRemoveDialogAction.setEnabled(value && (selectionModel.getMinSelectionIndex() >= 0));
    }

    private SearchableComboBox<BlockRecord> filterBox(final BiConsumer<Integer, Integer> setter) {
        final var box = new SearchableComboBox<>(Resources.get().getBlocks());
        box.setMaximumSize(Dimensions.MAXIMUM_COMBO_BOX_SIZE);
        box.setMinimumSize(Dimensions.MINIMUM_COMBO_BOX_SIZE);
        box.addActionListener(event -> {
            if (box.getSelectedItem() instanceof final BlockRecord block) {
                setter.accept(block.starts(), block.ends());
            } else {
                setter.accept(0, Integer.MAX_VALUE);
            }
        });
        return box;
    }

    private void setFilterLeft(final int from, final int to) {
        filterLeft = model -> model.getLeft().getCodePoint() >= from && model.getLeft().getCodePoint() <= to;
        listModel.setFilter(model -> filterLeft.test(model) && filterRight.test(model));
    }

    private void setFilterRight(final int from, final int to) {
        filterLeft = model -> model.getRight().getCodePoint() >= from
                && model.getRight().getCodePoint() <= to;
        listModel.setFilter(model -> filterLeft.test(model) && filterRight.test(model));
    }
}
