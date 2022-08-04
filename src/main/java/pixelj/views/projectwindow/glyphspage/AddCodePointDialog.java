package pixelj.views.projectwindow.glyphspage;

import java.awt.Color;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.ButtonGroup;
import javax.swing.JComponent;
import javax.swing.KeyStroke;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.formdev.flatlaf.FlatClientProperties;

import pixelj.graphics.BinaryImage;
import pixelj.models.Glyph;
import pixelj.models.Project;
import pixelj.resources.Resources;
import pixelj.util.ChangeableInt;
import pixelj.util.ReadOnlyBoolean;

public class AddCodePointDialog extends AddCodePointDialogBase {

    private static final String DEFAULT_TEXT = "0";

    private final ChangeableInt input = new ChangeableInt();
    private final ReadOnlyBoolean validInput = input.test(Resources.get()::hasScalar);
    private final Project project;
    private int base = 10;

    public AddCodePointDialog(final Frame owner, final Project project) {
        super(owner);
        this.project = project;

        final var radioGroup = new ButtonGroup();
        radioGroup.add(dec);
        radioGroup.add(hex);
        dec.setSelected(true);

        dec.addChangeListener(e -> {
            if (dec.isSelected()) {
                decInput();
            } else {
                hexInput();
            }
        });

        closeButton.addActionListener(event -> setVisible(false));
        addButton.addActionListener(this::onAdd);

        cpIn.getDocument().addDocumentListener(new DocumentListener() {

            @Override
            public void insertUpdate(final DocumentEvent e) {
                onCpInTextChanged();
            }

            @Override
            public void removeUpdate(final DocumentEvent e) {
                onCpInTextChanged();
            }

            @Override
            public void changedUpdate(final DocumentEvent e) {
                onCpInTextChanged();
            }

        });

        getRootPane().registerKeyboardAction(
                (e) -> setVisible(false),
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
                JComponent.WHEN_IN_FOCUSED_WINDOW
        );

        validInput.addChangeListener(this::onValidChanged);
        cpIn.setText(DEFAULT_TEXT);
        onValidChanged(false);
    }

    private void hexInput() {
        cpIn.setText(Integer.toHexString(input.getValue()));
        base = 16;
    }

    private void decInput() {
        cpIn.setText(Integer.toString(input.getValue()));
        base = 10;
    }

    private void onCpInTextChanged() {
        try {
            input.setValue(Integer.parseInt(cpIn.getText(), base));
        } catch (NumberFormatException e) {
            input.setValue(0);
        }
    }

    private void onValidChanged(final boolean value) {
        cpIn.putClientProperty(
                FlatClientProperties.OUTLINE,
                value ? null : Color.RED
        );
        addButton.setEnabled(value);
    }

    private void onAdd(final ActionEvent e) {
        final var res = Resources.get();
        final var cp = input.getValue();
        if (!res.hasScalar(cp)) {
            return;
        }
        final var scalar = res.getScalar(cp);
        final var settings = project.getDocumentSettings();
        project.getGlyphs().add(
                new Glyph(
                    scalar.codePoint(),
                    settings.defaultWidth(),
                    BinaryImage.of(settings.canvasWidth(), settings.canvasHeight(), true)
                )
        );
        cpIn.setText(DEFAULT_TEXT);
    }
}
