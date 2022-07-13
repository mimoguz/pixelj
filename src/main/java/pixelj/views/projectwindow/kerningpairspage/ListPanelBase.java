package pixelj.views.projectwindow.kerningpairspage;

import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

import com.formdev.flatlaf.FlatClientProperties;

import pixelj.models.BlockRecord;
import pixelj.models.KerningPair;
import pixelj.resources.Resources;
import pixelj.views.controls.SearchableComboBox;
import pixelj.views.shared.Borders;
import pixelj.views.shared.Components;
import pixelj.views.shared.Dimensions;

/**
 * List panel design.
 */
abstract class ListPanelBase extends JPanel {

    private static final String LIST_HEADER_STYLE = "h4";

    protected final JList<KerningPair> list = new JList<>();
    protected final JButton addButton = new JButton();
    protected final JButton removeButton = new JButton();
    protected final SearchableComboBox<BlockRecord> leftFilterBox = new SearchableComboBox<>(
            Resources.get().getBlocks()
    );
    protected final SearchableComboBox<BlockRecord> rightFilterBox = new SearchableComboBox<>(
            Resources.get().getBlocks()
    );

    ListPanelBase() {
        list.setCellRenderer(new KerningPairCellRenderer(48));

        Components.setFixedSize(addButton, Dimensions.TEXT_BUTTON_SIZE);
        Components.setFixedSize(removeButton, Dimensions.TEXT_BUTTON_SIZE);
        list.setMaximumSize(Dimensions.MAXIMUM);
        leftFilterBox.setMaximumSize(Dimensions.MAXIMUM_COMBO_BOX_SIZE);
        leftFilterBox.setMinimumSize(Dimensions.MINIMUM_COMBO_BOX_SIZE);
        rightFilterBox.setMaximumSize(Dimensions.MAXIMUM_COMBO_BOX_SIZE);
        rightFilterBox.setMinimumSize(Dimensions.MINIMUM_COMBO_BOX_SIZE);

        final var res = Resources.get();

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
        leftTitle.putClientProperty(FlatClientProperties.STYLE_CLASS, LIST_HEADER_STYLE);
        leftTitle.setHorizontalAlignment(SwingConstants.CENTER);
        filterPanel.add(leftTitle);

        final var rightTitle = new JLabel(res.getString("rightTitle"));
        rightTitle.putClientProperty(FlatClientProperties.STYLE_CLASS, LIST_HEADER_STYLE);
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
}
