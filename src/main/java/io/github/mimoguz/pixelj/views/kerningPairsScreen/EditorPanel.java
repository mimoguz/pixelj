package io.github.mimoguz.pixelj.views.kerningPairsScreen;

import io.github.mimoguz.pixelj.controls.StringView;
import io.github.mimoguz.pixelj.models.KerningPairModel;
import io.github.mimoguz.pixelj.resources.Resources;
import io.github.mimoguz.pixelj.views.shared.Components;
import io.github.mimoguz.pixelj.views.shared.Dimensions;

import com.formdev.flatlaf.FlatClientProperties;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class EditorPanel extends JPanel {
    private static final int INITIAL_ZOOM = 4;

    private final StringView preview;
    private final ArrayList<Integer> spaces = new ArrayList<>(java.util.List.of(0));
    private final JLabel title;
    private final JSpinner valueSpinner;
    private final JSlider zoomSlider;
    @Nullable
    private KerningPairModel model;
    private int spacing;

    public EditorPanel() {
        final var res = Resources.get();

        preview = new StringView(res.colors.disabledIcon());
        preview.setZoom(INITIAL_ZOOM);

        zoomSlider = new JSlider(1, 48, INITIAL_ZOOM);
        zoomSlider.setMinimumSize(new Dimension(96, 24));
        zoomSlider.setMaximumSize(new Dimension(256, 24));
        zoomSlider.setEnabled(false);
        zoomSlider.addChangeListener(e -> {
            if (zoomSlider.getValueIsAdjusting()) {
                preview.setZoom(zoomSlider.getValue());
            }
        });


        title = new JLabel(" ");
        title.putClientProperty(FlatClientProperties.STYLE_CLASS, "h4");

        valueSpinner = new JSpinner();
        Components.setFixedSize(valueSpinner, Dimensions.textButtonSize);
        valueSpinner.setEnabled(false);
        valueSpinner.setAlignmentY(0.5f);
        valueSpinner.addChangeListener(e -> {
            if (model == null) {
                return;
            }
            if (valueSpinner.getModel() instanceof SpinnerNumberModel numberModel) {
                model.setKerningValue(numberModel.getNumber().intValue());
                spaces.set(0, spacing + model.getKerningValue());
                preview.setSpaces(spaces);
            }
        });

        final var spinnerLabel = new JLabel(res.getString("kerningPairSpinnerLabel"));
        spinnerLabel.setEnabled(false);
        spinnerLabel.setAlignmentY(0.5f);

        final var pxLabel = new JLabel(res.getString("pixels"));
        pxLabel.setEnabled(false);
        pxLabel.setAlignmentY(0.5f);
    }

    public int getSpacing() {
        return spacing;
    }

    public void setSpacing(final int spacing) {
        this.spacing = spacing;
    }
}
