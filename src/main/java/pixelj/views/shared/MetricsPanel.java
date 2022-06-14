package pixelj.views.shared;

import pixelj.models.Metrics;
import pixelj.resources.Resources;
import pixelj.util.ChangeableInt;
import pixelj.util.ReadOnlyBoolean;

import com.formdev.flatlaf.FlatClientProperties;

import javax.swing.*;
import java.awt.*;

public class MetricsPanel extends JPanel {
    private final JSpinner ascender;
    private final Metrics.ValidatedBuilder builder;
    private final JSpinner canvasHeight;
    private final JSpinner canvasWidth;
    private final JSpinner capHeight;
    private final JSpinner defaultCharacterWidth;
    private final JSpinner descender;
    private final JCheckBox isMonospaced;
    private final JSpinner lineSpacing;
    private final JSpinner spaceSize;
    private final JSpinner spacing;
    private final JSpinner xHeight;

    public MetricsPanel(final Metrics init, final boolean canEditCanvasSize) {
        builder = Metrics.ValidatedBuilder.from(init);
        ascender = getSpinner(builder.ascender, builder.validAscender);
        canvasHeight = getSpinner(builder.canvasHeight, builder.validCanvasHeight);
        canvasWidth = getSpinner(builder.canvasWidth, builder.validCanvasWidth);
        capHeight = getSpinner(builder.capHeight, builder.validCapHeight);
        defaultCharacterWidth = getSpinner(builder.defaultCharacterWidth, builder.validDefaultCharacterWidth);
        descender = getSpinner(builder.descender, builder.validDescender);
        lineSpacing = getSpinner(builder.lineSpacing, builder.validLineSpacing);
        spaceSize = getSpinner(0, builder.spaceSize, builder.validSpaceSize);
        spacing = getSpinner(0, builder.spacing, builder.validSpacing);
        xHeight = getSpinner(0, builder.spaceSize, builder.validSpaceSize);

        isMonospaced = new JCheckBox();
        isMonospaced.addChangeListener(e -> builder.isMonospaced.setValue(isMonospaced.isSelected()));

        final var res = Resources.get();

        setLayout(new GridBagLayout());
        setBorder(Borders.EMPTY);
        final var cons = new GridBagConstraints();

        cons.gridx = 0;
        cons.gridy = 0;
        cons.weightx = 1.0;
        cons.insets = new Insets(0, 0, Dimensions.SMALL_PADDING, Dimensions.MEDIUM_PADDING);
        cons.anchor = GridBagConstraints.LINE_START;
        add(new JLabel(res.getString("metricsCanvasWidth")), cons);
        cons.gridy = GridBagConstraints.RELATIVE;
        add(new JLabel(res.getString("metricsCanvasHeight")), cons);
        add(new JLabel(res.getString("metricsAscender")), cons);
        add(new JLabel(res.getString("metricsDescender")), cons);
        add(new JLabel(res.getString("metricsCapHeight")), cons);
        add(new JLabel(res.getString("metricsXHeight")), cons);
        add(new JLabel(res.getString("metricsDefaultCharacterWidth")), cons);
        add(new JLabel(res.getString("metricsCharacterSpacing")), cons);
        add(new JLabel(res.getString("metricsSpaceSize")), cons);
        add(new JLabel(res.getString("metricsLineSpacing")), cons);
        final var isMonospacedLabel = new JLabel(res.getString("metricsIsMonospaced"));
        isMonospacedLabel.setBorder(BorderFactory.createEmptyBorder(8, 0, 8, 0));
        add(isMonospacedLabel, cons);

        cons.gridx = 1;
        cons.gridy = 0;
        cons.weightx = 0.0;
        cons.insets = new Insets(0, 0, Dimensions.SMALL_PADDING, 0);
        cons.fill = GridBagConstraints.NONE;
        cons.anchor = GridBagConstraints.LINE_END;
        add(canvasWidth, cons);
        cons.gridy = GridBagConstraints.RELATIVE;
        add(canvasHeight, cons);
        add(ascender, cons);
        add(descender, cons);
        add(capHeight, cons);
        add(xHeight, cons);
        add(defaultCharacterWidth, cons);
        add(spacing, cons);
        add(spaceSize, cons);
        add(lineSpacing, cons);
        add(isMonospaced, cons);

        setMetrics(init, canEditCanvasSize);
    }

    public Metrics getMetrics() throws Metrics.ValidatedBuilder.InvalidStateException {
        return builder.build();
    }

    public boolean isMetricsValid() {
        return builder.validAll.getValue();
    }

    public ReadOnlyBoolean metricsValidProperty() {
        return builder.validAll;
    }

    public void setMetrics(final Metrics metrics, boolean canEditCanvasSize) {
        canvasWidth.setValue(metrics.canvasWidth());
        canvasHeight.setValue(metrics.canvasHeight());
        ascender.setValue(metrics.ascender());
        descender.setValue(metrics.descender());
        capHeight.setValue(metrics.capHeight());
        xHeight.setValue(metrics.xHeight());
        defaultCharacterWidth.setValue(metrics.defaultCharacterWidth());
        spacing.setValue(metrics.spacing());
        spaceSize.setValue(metrics.spaceSize());
        lineSpacing.setValue(metrics.lineSpacing());
        isMonospaced.setSelected(metrics.isMonospaced());

        canvasHeight.setEnabled(canEditCanvasSize);
        canvasWidth.setEnabled(canEditCanvasSize);
    }

    private static JSpinner getSpinner(ChangeableInt value, ReadOnlyBoolean valid) {
        return getSpinner(1, value, valid);
    }

    private static JSpinner getSpinner(final int minimum, ChangeableInt value, ReadOnlyBoolean valid) {
        final var numberModel = new SpinnerNumberModel(value.getValue(), minimum, 512, 1);
        final var spinner = new JSpinner(numberModel);
        Components.setFixedSize(spinner, Dimensions.SPINNER_SIZE);
        spinner.addChangeListener(e -> value.setValue(numberModel.getNumber().intValue()));
        valid.addChangeListener(
                (sender, isValid) -> spinner.putClientProperty(
                        FlatClientProperties.OUTLINE,
                        isValid ? null : FlatClientProperties.OUTLINE_ERROR
                )
        );
        return spinner;
    }

    @SuppressWarnings("unused")
    private static int getValue(final JSpinner spinner) {
        return ((SpinnerNumberModel) spinner.getModel()).getNumber().intValue();
    }
}
