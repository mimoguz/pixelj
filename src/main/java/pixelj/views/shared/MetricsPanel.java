package pixelj.views.shared;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import com.formdev.flatlaf.FlatClientProperties;

import pixelj.models.Metrics;
import pixelj.resources.Resources;
import pixelj.util.ChangeableInt;
import pixelj.util.ReadOnlyBoolean;

public class MetricsPanel extends JPanel {
    private final JSpinner ascenderIn;
    private final Metrics.ValidatedBuilder builder;
    private final JSpinner canvasHeightIn;
    private final JSpinner canvasWidthIn;
    private final JSpinner capHeightIn;
    private final JSpinner defaultWidthIn;
    private final JSpinner descenderIn;
    private final JCheckBox isMonospaced;
    private final JSpinner lineSpacingIn;
    private final JSpinner spaceSizeIn;
    private final JSpinner letterSpacingIn;
    private final JSpinner xHeightIn;

    public MetricsPanel(final Metrics init, final boolean canEditCanvasSize) {
        builder = Metrics.ValidatedBuilder.from(init);
        ascenderIn = getSpinner(builder.ascender, builder.validAscender);
        canvasHeightIn = getSpinner(builder.canvasHeight, builder.validCanvasHeight);
        canvasWidthIn = getSpinner(builder.canvasWidth, builder.validCanvasWidth);
        capHeightIn = getSpinner(builder.capHeight, builder.validCapHeight);
        defaultWidthIn = getSpinner(builder.defaultWidth, builder.validDefaultWidth);
        descenderIn = getSpinner(builder.descender, builder.validDescender);
        lineSpacingIn = getSpinner(builder.lineSpacing, builder.validLineSpacing);
        spaceSizeIn = getSpinner(0, builder.spaceSize, builder.validSpaceSize);
        letterSpacingIn = getSpinner(0, builder.letterSpacing, builder.validLetterSpacing);
        xHeightIn = getSpinner(0, builder.spaceSize, builder.validSpaceSize);

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
        add(new JLabel(res.getString("metricsDefaultWidth")), cons);
        add(new JLabel(res.getString("metricsLetterSpacing")), cons);
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
        add(canvasWidthIn, cons);
        cons.gridy = GridBagConstraints.RELATIVE;
        add(canvasHeightIn, cons);
        add(ascenderIn, cons);
        add(descenderIn, cons);
        add(capHeightIn, cons);
        add(xHeightIn, cons);
        add(defaultWidthIn, cons);
        add(letterSpacingIn, cons);
        add(spaceSizeIn, cons);
        add(lineSpacingIn, cons);
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

    public void setMetrics(final Metrics metrics, final boolean canEditCanvasSize) {
        canvasWidthIn.setValue(metrics.canvasWidth());
        canvasHeightIn.setValue(metrics.canvasHeight());
        ascenderIn.setValue(metrics.ascender());
        descenderIn.setValue(metrics.descender());
        capHeightIn.setValue(metrics.capHeight());
        xHeightIn.setValue(metrics.xHeight());
        defaultWidthIn.setValue(metrics.defaultWidth());
        letterSpacingIn.setValue(metrics.letterSpacing());
        spaceSizeIn.setValue(metrics.spaceSize());
        lineSpacingIn.setValue(metrics.lineSpacing());
        isMonospaced.setSelected(metrics.isMonospaced());

        canvasHeightIn.setEnabled(canEditCanvasSize);
        canvasWidthIn.setEnabled(canEditCanvasSize);
    }

    private static JSpinner getSpinner(final ChangeableInt value, final ReadOnlyBoolean valid) {
        return getSpinner(1, value, valid);
    }

    private static JSpinner getSpinner(
            final int minimum,
            final ChangeableInt value,
            final ReadOnlyBoolean valid
    ) {
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
