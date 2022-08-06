package pixelj.views.projectwindow.kerningpairspage;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.Frame;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

import com.formdev.flatlaf.FlatClientProperties;

import net.miginfocom.swing.MigLayout;
import pixelj.models.Block;
import pixelj.models.Glyph;
import pixelj.resources.Resources;
import pixelj.views.controls.SearchableComboBox;
import pixelj.views.shared.Borders;
import pixelj.views.shared.Components;
import pixelj.views.shared.Dimensions;
import pixelj.views.shared.GlyphCell;
import pixelj.views.shared.GlyphCellRenderer;

/**  Add dialog design. */
public abstract class AddDialogBase extends JDialog {

    private static final String WRAP = "wrap";
    private static final String INSETS = "insets 2lp";

    protected final JComboBox<Block> filterBox = new SearchableComboBox<>(Resources.get().getBlocks());
    protected final JList<Glyph> glyphList = new JList<>();
    protected final GlyphCell leftCell = new GlyphCell(Dimensions.MAXIMUM_PREVIEW_SIZE);
    protected final GlyphCell rightCell = new GlyphCell(Dimensions.MAXIMUM_PREVIEW_SIZE);
    protected final JButton helpButton = new JButton();
    protected final JButton setLeftButton = new JButton(Resources.get().getString("setLeft"));
    protected final JButton setRightButton = new JButton(Resources.get().getString("setRight"));
    protected final JButton addButton = new JButton(Resources.get().getString("add"));
    protected final JButton cancelButton = new JButton(Resources.get().getString("close"));
    protected final JCheckBox mirroredCheck = new JCheckBox(Resources.get().getString("addMirrored"));

    AddDialogBase(final Frame owner) {
        super(
            owner,
            Resources.get().getString("kerningPairsAddDialogTitle"),
            Dialog.ModalityType.MODELESS
        );

        glyphList.setCellRenderer(new GlyphCellRenderer(Dimensions.MAXIMUM_PREVIEW_SIZE));
        Components.setFixedSize(addButton, Dimensions.TEXT_BUTTON_SIZE);
        Components.setFixedSize(setLeftButton, Dimensions.TEXT_BUTTON_SIZE);
        Components.setFixedSize(setRightButton, Dimensions.TEXT_BUTTON_SIZE);
        Components.setFixedSize(cancelButton, Dimensions.TEXT_BUTTON_SIZE);

        helpButton.putClientProperty(
            FlatClientProperties.BUTTON_TYPE,
            FlatClientProperties.BUTTON_TYPE_BORDERLESS
        );

        mirroredCheck.setIconTextGap(Dimensions.LARGE_PADDING);
        mirroredCheck.setHorizontalTextPosition(SwingConstants.LEADING);

        final var scrollPanel = new JScrollPane(glyphList);
        scrollPanel.setMaximumSize(Dimensions.MAXIMUM);

        final var centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(scrollPanel, BorderLayout.CENTER);

        final var mps = Dimensions.MAXIMUM_PREVIEW_SIZE;
        final var pad = Dimensions.MEDIUM_PADDING;

        final var selectionPanel = new JPanel(new MigLayout(
            INSETS,
            "[left, min!]" + pad + "lp[left, min!]",
            String.format("[center, %dlp!]%dlp[center, %dlp!]", mps, pad, mps, pad)
        ));
        selectionPanel.setBorder(Borders.EMPTY);
        selectionPanel.add(setLeftButton);
        selectionPanel.add(leftCell, WRAP);
        selectionPanel.add(setRightButton);
        selectionPanel.add(rightCell);
        centerPanel.add(selectionPanel, BorderLayout.SOUTH);

        final var buttonPanel = new JPanel(new MigLayout(
            INSETS,
            String.format("[grow, left]%dlp[]%dlp[]", pad, pad),
            "[center]" + pad * 2 + "lp[center]"
        ));
        buttonPanel.add(mirroredCheck, "spanx 3, right, wrap");
        buttonPanel.add(helpButton);
        buttonPanel.add(addButton);
        buttonPanel.add(cancelButton);

        final var content = new JPanel();
        content.setLayout(new BorderLayout(Dimensions.MEDIUM_PADDING, Dimensions.MEDIUM_PADDING));

        content.add(filterBox, BorderLayout.NORTH);
        content.add(centerPanel, BorderLayout.CENTER);
        content.add(buttonPanel, BorderLayout.SOUTH);
        content.setBorder(Borders.MEDIUM_EMPTY);
        setContentPane(content);
        getRootPane().setDefaultButton(addButton);

        pack();
        glyphList.requestFocusInWindow();
        setSize(400, 720);
        setResizable(true);
        setAlwaysOnTop(true);
    }

    /**
    * @param visible Is visible. Note that since this dialog is modal, passing true here block the caller.
    */
    @Override
    public void setVisible(final boolean visible) {
        if (visible) {
            setLocationRelativeTo(getOwner());
            glyphList.requestFocusInWindow();
        }
        super.setVisible(visible);
    }
}
