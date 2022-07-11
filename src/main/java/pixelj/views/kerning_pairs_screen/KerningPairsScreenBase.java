package pixelj.views.kerning_pairs_screen;

import javax.swing.JSplitPane;

import pixelj.views.shared.Dimensions;

public abstract class KerningPairsScreenBase extends JSplitPane {
    protected final EditorPanel editorPanel;
    protected final ListPanel listPanel;

    public KerningPairsScreenBase(final EditorPanel editorPanel, final ListPanel listPanel) {
        this.editorPanel = editorPanel;
        this.listPanel = listPanel;

        setMaximumSize(Dimensions.MAXIMUM);
        setLeftComponent(editorPanel);
        setRightComponent(listPanel);
        setResizeWeight(1.0);
    }

    /**
     * Already handles the focus issue.
     * 
     * @param enabled Is Enabled
     */
    @Override
    public void setEnabled(final boolean enabled) {
        super.setEnabled(enabled);
        listPanel.setEnabled(enabled);
        editorPanel.setEnabled(enabled);
        // Take focus away from inputs:
        if (enabled) {
            listPanel.requestFocus();
        }
    }
}
