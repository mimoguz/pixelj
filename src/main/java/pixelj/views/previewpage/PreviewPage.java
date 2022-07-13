package pixelj.views.previewpage;

import java.awt.event.ActionEvent;

import javax.swing.JComponent;

import pixelj.actions.Actions;
import pixelj.actions.PreviewScreenActions;
import pixelj.models.Project;

public final class PreviewPage extends PreviewPageBase {
    private final PreviewScreenActions actions;

    public PreviewPage(final Project project, final JComponent root) {
        actions = new PreviewScreenActions(project, textInput, container);
        actions.setZoom(INITIAL_ZOOM);
        Actions.registerShortcuts(actions.all, root);
        refreshButton.setAction(actions.refreshAction);
        clearButton.setAction(actions.clearAction);

        final var zoomSlider = zoomStrip.getSlider();
        zoomSlider.addChangeListener(e -> {
            if (zoomSlider.getValueIsAdjusting()) {
                actions.setZoom(zoomSlider.getValue());
            }
        });

        project.documentSettingsProperty.addChangeListener((source, settings) -> {
            if (isEnabled()) {
                refresh();
            }
        });
    }

    /**
     * Redraw preview.
     */
    public void refresh() {
        actions.refreshAction.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, ""));
    }

    @Override
    public void setEnabled(final boolean value) {
        super.setEnabled(value);
        Actions.setEnabled(actions.all, value);
        if (value) {
            refresh();
        }
    }
}
