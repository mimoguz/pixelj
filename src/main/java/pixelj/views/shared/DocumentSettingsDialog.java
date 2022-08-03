package pixelj.views.shared;

import java.awt.Frame;
import java.awt.event.KeyEvent;

import javax.swing.JComponent;
import javax.swing.JSpinner;
import javax.swing.KeyStroke;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.formdev.flatlaf.FlatClientProperties;

import pixelj.actions.ApplicationAction;
import pixelj.models.DocumentSettings;
import pixelj.resources.Icons;
import pixelj.resources.Resources;
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

        setupSpinner(canvasWidthIn, 1, builder.canvasWidth, builder.validCanvasWidth);
        setupSpinner(canvasHeightIn, 1, builder.canvasHeight, builder.validCanvasHeight);
        setupSpinner(ascenderIn, 0, builder.ascender, builder.validAscender);
        setupSpinner(descenderIn, 0, builder.descender, builder.validDescender);
        setupSpinner(capHeightIn, 0, builder.capHeight, builder.validCapHeight);
        setupSpinner(xHeightIn, 0, builder.xHeight, builder.validXHeight);
        setupSpinner(defaultWidthIn, 0, builder.defaultWidth, builder.validDefaultWidth);
        setupSpinner(letterSpacingIn, 0, builder.letterSpacing, builder.validLetterSpacing);
        setupSpinner(spaceSizeIn, 0, builder.spaceSize, builder.validSpaceSize);
        setupSpinner(lineSpacingIn, 0, builder.lineSpacing, builder.validLineSpacing);

        isBoldIn.addChangeListener(e -> builder.isBold.setValue(isBoldIn.isSelected()));
        isItalicIn.addChangeListener(e -> builder.isItalic.setValue(isItalicIn.isSelected()));
        isMonospacedIn.addChangeListener(e -> {
            final var isSelected = isMonospacedIn.isSelected();
            builder.isMonospaced.setValue(isSelected);
            spaceSizeIn.setEnabled(!isSelected);
        });

        titleIn.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(final DocumentEvent e) {
                builder.setTitle(titleIn.getText());
            }

            @Override
            public void removeUpdate(final DocumentEvent e) {
                builder.setTitle(titleIn.getText());
            }

            @Override
            public void changedUpdate(final DocumentEvent e) {
                builder.setTitle(titleIn.getText());
            }
        });

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
        helpButton.setAction(
                new ApplicationAction(
                        "documentSettingsHelpAction",
                        (e, action) -> Help.showPage(Help.Page.DOCUMENT_SETTINGS)
                )
                        .setIcon(Icons.HELP)
                        .setTooltip(Resources.get().getString("help"))
        );

        getRootPane().registerKeyboardAction(
                (e) -> setVisible(false),
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
                JComponent.WHEN_IN_FOCUSED_WINDOW
        );
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
        spinner.setModel(numberModel);
        spinner.addChangeListener(e -> value.setValue(numberModel.getNumber().intValue()));
        valid.addChangeListener((sender, isValid) -> spinner.putClientProperty(
                FlatClientProperties.OUTLINE,
                isValid ? null : FlatClientProperties.OUTLINE_ERROR
        ));
    }
}
