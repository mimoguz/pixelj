package io.github.mimoguz.pixelj.views.kerning_pairs_screen;

import io.github.mimoguz.pixelj.models.KerningPair;
import io.github.mimoguz.pixelj.resources.Resources;
import io.github.mimoguz.pixelj.util.Detachable;
import io.github.mimoguz.pixelj.views.controls.StringView;
import io.github.mimoguz.pixelj.views.controls.ZoomStrip;
import io.github.mimoguz.pixelj.views.shared.Borders;
import io.github.mimoguz.pixelj.views.shared.Components;
import io.github.mimoguz.pixelj.views.shared.Dimensions;

import com.formdev.flatlaf.FlatClientProperties;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class EditorPanel extends JPanel implements Detachable {
    private static final int INITIAL_ZOOM = 4;
    private final StringView preview = new StringView(Resources.get().colors.disabledIcon());
    private final JLabel pxLabel;
    private final ArrayList<Integer> spaces = new ArrayList<>(java.util.List.of(0));
    private final JLabel spinnerLabel;
    private final JSpinner valueSpinner;
    private final ZoomStrip zoomStrip = new ZoomStrip(1, 48, INITIAL_ZOOM);
    private KerningPair model;
    private int spacing;

    public EditorPanel() {
        final var res = Resources.get();

        preview.setPadding(Dimensions.MEDIUM_PADDING);
        preview.setZoom(INITIAL_ZOOM);

        final var zoomSlider = zoomStrip.getSlider();
        zoomSlider.addChangeListener(e -> {
            if (zoomSlider.getValueIsAdjusting()) {
                preview.setZoom(zoomSlider.getValue());
            }
        });
        zoomStrip.setEnabled(false);

        valueSpinner = new JSpinner();
        Components.setFixedSize(valueSpinner, Dimensions.SPINNER_SIZE);
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

        spinnerLabel = new JLabel(res.getString("kerningValue"));
        spinnerLabel.setAlignmentY(0.5f);

        pxLabel = new JLabel(res.getString("pixels"));
        pxLabel.setAlignmentY(0.5f);

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        final var title = new JLabel(res.getString("kerningValueEditorTitle"));
        title.putClientProperty(FlatClientProperties.STYLE_CLASS, "h3");
        final var titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.X_AXIS));
        titlePanel.setBorder(Borders.TITLE_CENTER);
//        titlePanel.add(Box.createHorizontalGlue());
        titlePanel.add(Box.createHorizontalStrut(Dimensions.LARGE_PADDING));
        titlePanel.add(title);
        titlePanel.add(Box.createHorizontalGlue());
        add(titlePanel);

        final var previewPanel = new JPanel(new GridBagLayout());
        previewPanel.add(preview);
        final var scrollPanel = new JScrollPane(previewPanel);
        // To balance the split pane divider
        scrollPanel.setBorder(Borders.SMALL_EMPTY_CUP_CENTER);
        scrollPanel.setMaximumSize(Dimensions.MAXIMUM);
        add(scrollPanel);

        final var spinnerPanel = new JPanel();
        spinnerPanel.setLayout(new BoxLayout(spinnerPanel, BoxLayout.X_AXIS));
        spinnerPanel.setBorder(Borders.SMALL_EMPTY_BOTTOM_CENTER_PANEL);
        spinnerPanel.add(Box.createHorizontalGlue());
        spinnerPanel.add(spinnerLabel);
        spinnerPanel.add(Box.createRigidArea(Dimensions.SMALL_SQUARE));
        spinnerPanel.add(valueSpinner);
        spinnerPanel.add(Box.createRigidArea(Dimensions.SMALL_SQUARE));
        spinnerPanel.add(pxLabel);
        spinnerPanel.add(Box.createHorizontalGlue());
        add(spinnerPanel);

        add(zoomStrip);

        setEnabled(false);
    }

    @Override
    public void detach() {
        setModel(null);
    }

    public StringView getPreview() {
        return preview;
    }

    public int getSpacing() {
        return spacing;
    }

    public void setSpacing(final int value) {
        spacing = value;
        if (model == null) {
            return;
        }
        valueSpinner.setModel(
                new SpinnerNumberModel(
                        model.getKerningValue(),
                        -model.getLeft().getWidth() - spacing,
                        model.getRight().getWidth(),
                        1
                )
        );
        spaces.set(0, model.getKerningValue() + spacing);
        preview.setSpaces(spaces);
    }

    @Override
    public void setEnabled(boolean value) {
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
            valueSpinner.setModel(
                    new SpinnerNumberModel(
                            model.getKerningValue(),
                            -model.getLeft().getWidth() - spacing,
                            model.getRight().getWidth(),
                            1
                    )
            );
            setEnabled(true);
        }
    }
}
