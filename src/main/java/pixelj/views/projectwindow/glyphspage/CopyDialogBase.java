package pixelj.views.projectwindow.glyphspage;

import com.formdev.flatlaf.FlatClientProperties;
import pixelj.models.Block;
import pixelj.models.Glyph;
import pixelj.resources.Resources;
import pixelj.views.controls.SearchableComboBox;
import pixelj.views.shared.Borders;
import pixelj.views.shared.Components;
import pixelj.views.shared.Dimensions;
import pixelj.views.shared.GlyphCellRenderer;

import javax.swing.*;
import java.awt.*;

/**  Add dialog design. */
public abstract class CopyDialogBase extends JDialog {

    protected final JComboBox<Block> filterBox = new SearchableComboBox<>(Resources.get().getBlocks());
    protected final JList<Glyph> glyphList = new JList<>();
    protected final JButton helpButton = new JButton();
    protected final JButton copyButton = new JButton(Resources.get().getString("copy"));
    protected final JButton cancelButton = new JButton(Resources.get().getString("cancel"));

    CopyDialogBase(final Frame owner) {
        super(
            owner,
            Resources.get().getString("copyDialogTitle"),
            ModalityType.APPLICATION_MODAL
        );

        glyphList.setCellRenderer(new GlyphCellRenderer(Dimensions.MAXIMUM_PREVIEW_SIZE));
        Components.setFixedSize(copyButton, Dimensions.TEXT_BUTTON_SIZE);
        Components.setFixedSize(cancelButton, Dimensions.TEXT_BUTTON_SIZE);

        helpButton.putClientProperty(
            FlatClientProperties.BUTTON_TYPE,
            FlatClientProperties.BUTTON_TYPE_BORDERLESS
        );


        final var scrollPanel = new JScrollPane(glyphList);
        scrollPanel.setMaximumSize(Dimensions.MAXIMUM);

        final var buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.add(helpButton);
        buttonPanel.add(Box.createHorizontalGlue());
        buttonPanel.add(copyButton);
        buttonPanel.add(cancelButton);

        final var content = new JPanel();
        content.setLayout(new BorderLayout(Dimensions.MEDIUM_PADDING, Dimensions.MEDIUM_PADDING));

        content.add(filterBox, BorderLayout.NORTH);
        content.add(scrollPanel, BorderLayout.CENTER);
        content.add(buttonPanel, BorderLayout.SOUTH);
        content.setBorder(Borders.MEDIUM_EMPTY);
        setContentPane(content);
        getRootPane().setDefaultButton(copyButton);

        pack();
        glyphList.requestFocusInWindow();
        setSize(400, 720);
        setResizable(true);
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
