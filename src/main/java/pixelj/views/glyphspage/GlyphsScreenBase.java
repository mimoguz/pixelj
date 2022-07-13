package pixelj.views.glyphspage;

import javax.swing.JSplitPane;

import pixelj.views.shared.Dimensions;

/**
 * GlyphsScreen design.
 */
abstract class GlyphsScreenBase extends JSplitPane {
    protected final ListPanel listPanel;
    protected final PainterPanel painterPanel;

    GlyphsScreenBase(final ListPanel listPanel, final PainterPanel painterPanel) {
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
