package pixelj.views.projectwindow.glyphspage;

import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import pixelj.models.Block;
import pixelj.models.Glyph;
import pixelj.resources.Resources;
import pixelj.views.controls.CoupledActionsButton;
import pixelj.views.controls.SearchableComboBox;
import pixelj.views.shared.Borders;
import pixelj.views.shared.Components;
import pixelj.views.shared.Dimensions;
import pixelj.views.shared.GlyphCellRenderer;

/** ListPanel design. */
abstract class ListPanelBase extends JPanel {

    protected final JComboBox<Block> filterBox = new SearchableComboBox<>(Resources.get().getBlocks());
    protected final JList<Glyph> list = new JList<>();
    protected final CoupledActionsButton addButton = new CoupledActionsButton();
    protected final JButton removeButton = new JButton();

    ListPanelBase() {
        Components.setFixedSize(addButton, Dimensions.TEXT_BUTTON_SIZE);
        Components.setFixedSize(removeButton, Dimensions.TEXT_BUTTON_SIZE);

        list.setCellRenderer(new GlyphCellRenderer(48));
        list.setMaximumSize(Dimensions.MAXIMUM);

        filterBox.setMaximumSize(Dimensions.MAXIMUM_COMBO_BOX_SIZE);
        filterBox.setMinimumSize(Dimensions.MINIMUM_COMBO_BOX_SIZE);

        setBorder(Borders.EMPTY);
        setPreferredSize(new Dimension(300, 100));
        setMinimumSize(new Dimension(220, 100));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createMatteBorder(0, 1, 0, 0, Resources.get().colors.separator()));

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
}
