package pixelj.views.projectwindow.glyphspage;

import java.awt.*;

import javax.swing.*;

import com.formdev.flatlaf.FlatClientProperties;
import pixelj.models.Block;
import pixelj.models.Glyph;
import pixelj.resources.Resources;
import pixelj.views.controls.CoupledActionsButton;
import pixelj.views.controls.SearchableComboBox;
import pixelj.views.shared.Borders;
import pixelj.views.shared.Components;
import pixelj.views.shared.Dimensions;
import pixelj.views.shared.GlyphCellRenderer;

/**
 * ListPanel design.
 */
abstract class ListPanelBase extends JPanel {

    protected final JComboBox<Block> filterBox = new SearchableComboBox<>(Resources.get().getBlocks());
    protected final JList<Glyph> list = new JList<>();
    protected final CoupledActionsButton addButton = new CoupledActionsButton();
    protected final JButton removeButton = new JButton();
    protected final JButton gridViewButton = new JButton();
    protected final JButton copyFromButton = new JButton();
    protected final JPopupMenu gridViewPopup = new JPopupMenu();
    protected final JPanel gridView = new JPanel(new GridLayout(10, 10, 1, 1));

    ListPanelBase() {
        Components.setFixedSize(addButton, Dimensions.TEXT_BUTTON_SIZE);
        Components.setFixedSize(removeButton, Dimensions.TEXT_BUTTON_SIZE);

        Components.setFixedSize(
            gridViewButton,
            new Dimension(Dimensions.TEXT_BUTTON_SIZE.height, Dimensions.TEXT_BUTTON_SIZE.height)
        );
        gridViewButton.setFocusable(false);

        Components.setFixedSize(
            copyFromButton,
            new Dimension(Dimensions.TEXT_BUTTON_SIZE.height, Dimensions.TEXT_BUTTON_SIZE.height)
        );
        copyFromButton.setFocusable(false);

        list.setCellRenderer(new GlyphCellRenderer(48));
        list.setMaximumSize(Dimensions.MAXIMUM);

        filterBox.setMaximumSize(Dimensions.MAXIMUM_COMBO_BOX_SIZE);
        filterBox.setMinimumSize(Dimensions.MINIMUM_COMBO_BOX_SIZE);

        setBorder(Borders.EMPTY);
        setPreferredSize(new Dimension(360, 100));
        setMinimumSize(new Dimension(288, 100));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createMatteBorder(0, 1, 0, 0, Resources.get().colors.separator()));

        final var buttonPanel = new JPanel();
        buttonPanel.setBorder(Borders.MEDIUM_EMPTY);
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.add(Box.createHorizontalGlue());
        buttonPanel.add(addButton);
        buttonPanel.add(Box.createHorizontalStrut(Dimensions.MEDIUM_PADDING));
        buttonPanel.add(removeButton);
        buttonPanel.add(Box.createHorizontalStrut(Dimensions.MEDIUM_PADDING));
        buttonPanel.add(Box.createHorizontalGlue());
        buttonPanel.add(copyFromButton);
        buttonPanel.add(Box.createHorizontalStrut(Dimensions.SMALL_PADDING));
        buttonPanel.add(gridViewButton);
        add(buttonPanel);

        final var filterPanel = new JPanel();
        filterPanel.setBorder(Borders.MEDIUM_EMPTY_CUP);
        filterPanel.setLayout(new BoxLayout(filterPanel, BoxLayout.X_AXIS));
        filterPanel.add(filterBox);
        add(filterPanel);

        final var scrollPanel = new JScrollPane(list);
        scrollPanel.setMaximumSize(Dimensions.MAXIMUM);
        scrollPanel.setBorder(Borders.EMPTY);
        add(scrollPanel);

        gridView.setBackground(Resources.get().colors.separator());
        final var gridViewScroll = new JScrollPane(gridView);
        gridViewScroll.setBorder(Borders.EMPTY);
        gridViewScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        gridViewPopup.add(gridViewScroll);
        gridViewPopup.setPopupSize(500, 500);
    }
}
