package pixelj.views.glyphs_screen;

import javax.swing.JComponent;
import javax.swing.JSplitPane;

import pixelj.models.Project;
import pixelj.views.shared.Dimensions;

/**
 * GlyphsScreen design. This should be extended to add functionality.
 */
abstract class GlyphsScreenBase extends JSplitPane {
    protected final ListPanel listPanel;
    protected final PainterPanel painterPanel;

    GlyphsScreenBase(
                final Project project,
                final JComponent root,
                final ListPanel listPanel,
                final PainterPanel painterPanel
    ) {
        this.listPanel = listPanel;
        this.painterPanel = painterPanel;
        setMaximumSize(Dimensions.MAXIMUM);
        setLeftComponent(painterPanel);
        setRightComponent(listPanel);
        setResizeWeight(1.0);
    }

    /**
     * This will enable/disable the listPanel and painterPanel too.
     */
    @Override
    public void setEnabled(final boolean value) {
        listPanel.setEnabled(value);
        painterPanel.setEnabled(value);
        super.setEnabled(value);
    }
}
