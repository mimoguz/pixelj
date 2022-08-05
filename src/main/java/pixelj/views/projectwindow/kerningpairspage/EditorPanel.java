package pixelj.views.projectwindow.kerningpairspage;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseWheelEvent;
import java.util.ArrayList;

import javax.swing.SpinnerNumberModel;

import pixelj.models.DocumentSettings;
import pixelj.models.KerningPair;
import pixelj.models.Project;
import pixelj.views.shared.ZoomAdapter;

public final class EditorPanel extends EditorPanelBase {
    private final ArrayList<Integer> spaces = new ArrayList<>(java.util.List.of(0));
    private KerningPair model;
    private int spacing;

    public EditorPanel(final Project project) {
        final var zoomSlider = zoomStrip.getSlider();
        zoomSlider.addChangeListener(e -> preview.setZoom(zoomSlider.getValue()));
        zoomStrip.setEnabled(false);
        final var mouseAdapter = new ZoomAdapter(zoomSlider);
        scrollPane.addMouseWheelListener(mouseAdapter);

        valueSpinner.addChangeListener(e -> {
            if (model == null) {
                return;
            }
            if (valueSpinner.getModel() instanceof final SpinnerNumberModel numberModel) {
                model.setKerningValue(numberModel.getNumber().intValue());
                spaces.set(0, spacing + model.getKerningValue());
                preview.setSpaces(spaces);
            }
        });

        setMetrics(project.getDocumentSettings());
        project.documentSettingsProperty.addChangeListener((source, settings) -> setMetrics(settings));
        setEnabled(false);
    }

    @Override
    public void setEnabled(final boolean value) {
        super.setEnabled(value);
        final var valueIfNotNull = value && (model != null);
        valueSpinner.setEnabled(valueIfNotNull);
        zoomStrip.setEnabled(valueIfNotNull);
        spinnerLabel.setEnabled(valueIfNotNull);
        pxLabel.setEnabled(valueIfNotNull);
        if (value) {
            preview.updateView();
        }
    }

    /**
     * @param value May be null.
     */
    public void setModel(final KerningPair value) {
        if (model == value) {
            return;
        }
        model = value;
        if (model == null) {
            preview.set(java.util.Collections.emptyList(), spaces);
            setEnabled(false);
        } else {
            spaces.set(0, spacing + model.getKerningValue());
            preview.set(java.util.List.of(model.getLeft(), model.getRight()), spaces);
            valueSpinner.setModel(new SpinnerNumberModel(
                model.getKerningValue(),
                -model.getLeft().getWidth() - spacing,
                model.getRight().getWidth(),
                1
            ));
            setEnabled(true);
        }
    }

    private void setMetrics(final DocumentSettings settings) {
        spacing = settings.letterSpacing();
        if (model == null) {
            return;
        }
        valueSpinner.setModel(new SpinnerNumberModel(
            model.getKerningValue(),
            -model.getLeft().getWidth() - spacing,
            model.getRight().getWidth(),
            1
        ));
        spaces.set(0, model.getKerningValue() + spacing);
        preview.setMaxY(settings.descender() + settings.ascender());
        preview.setSpaces(spaces);
    }
}
