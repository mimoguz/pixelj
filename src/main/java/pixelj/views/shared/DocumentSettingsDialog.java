package pixelj.views.shared;

import java.awt.Frame;

import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import com.formdev.flatlaf.FlatClientProperties;

import pixelj.models.DocumentSettings;
import pixelj.util.ChangeableInt;
import pixelj.util.ReadOnlyBoolean;

public final class DocumentSettingsDialog extends DocumentSettingsDialogBase {

    private DocumentSettings result;
    private final DocumentSettings.Builder builder = DocumentSettings.Builder.getDefaultBuilder();

    public DocumentSettingsDialog(
            final Frame owner,
            final String dialogTitle,
            final String applyButtonLabel,
            final boolean canEditCanvasSize
    ) {
        super(owner, dialogTitle, applyButtonLabel);
        canvasWidthIn.setEnabled(canEditCanvasSize);
        canvasHeightIn.setEnabled(canEditCanvasSize);

        setupSpinner(canvasWidthIn, 1, builder.canvasWidth, builder.validAscender);
        setupSpinner(canvasHeightIn, 1, builder.canvasHeight, builder.validAscender);
        setupSpinner(ascenderIn, 0, builder.ascender, builder.validAscender);
        setupSpinner(descenderIn, 0, builder.descender, builder.validAscender);
        setupSpinner(capHeightIn, 0, builder.capHeight, builder.validAscender);
        setupSpinner(xHeightIn, 0, builder.xHeight, builder.validAscender);
        setupSpinner(defaultWidthIn, 0, builder.defaultWidth, builder.validAscender);
        setupSpinner(letterSpacingIn, 0, builder.letterSpacing, builder.validAscender);
        setupSpinner(spaceSizeIn, 0, builder.spaceSize, builder.validAscender);
        setupSpinner(spaceSizeIn, 0, builder.spaceSize, builder.validAscender);
        setupSpinner(lineSpacingIn, 0, builder.lineSpacing, builder.validAscender);

        isBoldIn.addChangeListener(e -> builder.isBold.setValue(isBoldIn.isSelected()));
        isItalicIn.addChangeListener(e -> builder.isItalic.setValue(isItalicIn.isSelected()));
        isMonospacedIn.addChangeListener(e -> builder.isMonospaced.setValue(isMonospacedIn.isSelected()));

        builder.validAll.addChangeListener((sender, value) -> applyButton.setEnabled(value));
        applyButton.addActionListener(event -> {
            if (builder.validAll.getValue()) {
                try {
                    result = builder.build();
                } catch (DocumentSettings.Builder.InvalidStateException e) {  // Should be unreachable
                    e.printStackTrace();
                }
                setVisible(false);
            }
        });
        cancelButton.addActionListener(event -> {
            result = null;
            setVisible(false);
        });
    }

    /**
     * Set the dialog fields copying the source.
     * 
     * @param settings Source
     */
    public void set(final DocumentSettings settings) {
        titleIn.setText(settings.title());
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
    }

    public DocumentSettings getResult() {
        return result;
    }

    @Override
    public void setVisible(final boolean visible) {
        if (visible) {
            result = null;
        }
        super.setVisible(visible);
    }

    private static void setupSpinner(
            final JSpinner spinner,
            final int minimum,
            final ChangeableInt value,
            final ReadOnlyBoolean valid
    ) {
        final var numberModel = new SpinnerNumberModel(value.getValue(), minimum, 512, 1);
        Components.setFixedSize(spinner, Dimensions.SPINNER_SIZE);
        spinner.addChangeListener(e -> value.setValue(numberModel.getNumber().intValue()));
        valid.addChangeListener(
                (sender, isValid) -> spinner.putClientProperty(
                        FlatClientProperties.OUTLINE,
                        isValid ? null : FlatClientProperties.OUTLINE_ERROR
                )
        );
    }
}
