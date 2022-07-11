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

import pixelj.models.DocumentSettings;
import pixelj.resources.Resources;
import pixelj.util.ChangeableInt;
import pixelj.util.ReadOnlyBoolean;

public class DocumentSettingsPanel extends JPanel {
    private final JSpinner ascenderIn;
    private final DocumentSettings.Builder builder;
    private final JSpinner canvasHeightIn;
    private final JSpinner canvasWidthIn;
    private final JSpinner capHeightIn;
    private final JSpinner defaultWidthIn;
    private final JSpinner descenderIn;
    private final JCheckBox isBoldIn;
    private final JCheckBox isItalicIn;
    private final JCheckBox isMonospacedIn;
    private final JSpinner lineSpacingIn;
    private final JSpinner spaceSizeIn;
    private final JSpinner letterSpacingIn;
    private final JSpinner xHeightIn;

    public DocumentSettingsPanel(final DocumentSettings init, final boolean canEditCanvasSize) {
        builder = DocumentSettings.Builder.from(init);
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

        isBoldIn = new JCheckBox();
        isBoldIn.addChangeListener(e -> builder.isBold.setValue(isBoldIn.isSelected()));
        isItalicIn = new JCheckBox();
        isItalicIn.addChangeListener(e -> builder.isItalic.setValue(isItalicIn.isSelected()));
        isMonospacedIn = new JCheckBox();
        isMonospacedIn.addChangeListener(e -> builder.isMonospaced.setValue(isMonospacedIn.isSelected()));

        final var res = Resources.get();

        setLayout(new GridBagLayout());
        setBorder(Borders.EMPTY);
        final var cons = new GridBagConstraints();

        cons.gridx = 0;
        cons.gridy = 0;
        cons.weightx = 1.0;
        cons.insets = new Insets(0, 0, Dimensions.SMALL_PADDING, Dimensions.MEDIUM_PADDING);
        cons.anchor = GridBagConstraints.LINE_START;
        add(new JLabel(res.getString("documentCanvasWidth")), cons);
        cons.gridy = GridBagConstraints.RELATIVE;
        add(new JLabel(res.getString("documentCanvasHeight")), cons);
        add(new JLabel(res.getString("documentAscender")), cons);
        add(new JLabel(res.getString("documentDescender")), cons);
        add(new JLabel(res.getString("documentCapHeight")), cons);
        add(new JLabel(res.getString("documentXHeight")), cons);
        add(new JLabel(res.getString("documentDefaultWidth")), cons);
        add(new JLabel(res.getString("documentLetterSpacing")), cons);
        add(new JLabel(res.getString("documentSpaceSize")), cons);
        add(new JLabel(res.getString("documentLineSpacing")), cons);
        final var labelBorder = BorderFactory.createEmptyBorder(8, 0, 8, 0);
        final var isMonospacedLabel = new JLabel(res.getString("documentIsMonospaced"));
        isMonospacedLabel.setBorder(labelBorder);
        add(isMonospacedLabel, cons);
        final var isBoldLabel = new JLabel(res.getString("documentIsBold"));
        isBoldLabel.setBorder(labelBorder);
        add(isBoldLabel, cons);
        final var isItalicLabel = new JLabel(res.getString("documentIsItalic"));
        isItalicLabel.setBorder(labelBorder);
        add(isItalicLabel, cons);

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
        add(isMonospacedIn, cons);
        add(isBoldIn, cons);
        add(isItalicIn, cons);

        setSettings(init, canEditCanvasSize);
    }

    public DocumentSettings getDocumentSettings() throws DocumentSettings.Builder.InvalidStateException {
        return builder.build();
    }

    public boolean isMetricsValid() {
        return builder.validAll.getValue();
    }

    public ReadOnlyBoolean settingsValidProperty() {
        return builder.validAll;
    }

    public void setSettings(final DocumentSettings settings, final boolean canEditCanvasSize) {
        canvasWidthIn.setValue(settings.canvasWidth());
        canvasHeightIn.setValue(settings.canvasHeight());
        ascenderIn.setValue(settings.ascender());
        descenderIn.setValue(settings.descender());
        capHeightIn.setValue(settings.capHeight());
        xHeightIn.setValue(settings.xHeight());
        defaultWidthIn.setValue(settings.defaultWidth());
        letterSpacingIn.setValue(settings.letterSpacing());
        spaceSizeIn.setValue(settings.spaceSize());
        lineSpacingIn.setValue(settings.lineSpacing());
        isMonospacedIn.setSelected(settings.isMonospaced());
        isBoldIn.setSelected(settings.isBold());
        isItalicIn.setSelected(settings.isItalic());

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
