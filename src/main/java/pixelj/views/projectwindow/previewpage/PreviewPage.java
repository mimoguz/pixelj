package pixelj.views.projectwindow.previewpage;

import java.awt.event.ActionEvent;

import javax.swing.JFrame;

import pixelj.actions.PreviewPageActions;
import pixelj.models.Project;
import pixelj.services.AppState;
import pixelj.views.shared.ZoomAdapter;

public final class PreviewPage extends PreviewPageBase {
    private final PreviewPageActions actions;

    public PreviewPage(final Project project, final AppState appState, final JFrame window) {
        actions = new PreviewPageActions(project, appState, textInput, container);
        actions.setZoom(INITIAL_ZOOM);
        actions.registerShortcuts(window.getRootPane());
        refreshButton.setAction(actions.refreshAction);
        clearButton.setAction(actions.clearAction);

        textInput.setText(appState.getPreviewText());

        final var zoomSlider = zoomStrip.getSlider();
        zoomSlider.addChangeListener(e -> actions.setZoom(zoomSlider.getValue()));
        final var mouseAdapter = new ZoomAdapter(zoomSlider);
        scrollPane.addMouseWheelListener(mouseAdapter);

        project.documentSettingsProperty.addChangeListener((source, settings) -> {
            if (isEnabled()) {
                refresh();
            }
        });
    }

    /** Redraw preview. */
    public void refresh() {
        actions.refreshAction.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, ""));
    }

    @Override
    public void setEnabled(final boolean enabled) {
        super.setEnabled(enabled);
        actions.setEnabled(enabled);
        if (enabled) {
            refresh();
        }
    }
}
