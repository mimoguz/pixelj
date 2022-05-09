package io.github.mimoguz.pixelj.views.characters_screen;

import java.awt.Font;

import javax.swing.*;

import com.formdev.flatlaf.FlatClientProperties;

import io.github.mimoguz.pixelj.models.CharacterModel;
import io.github.mimoguz.pixelj.models.Metrics;
import io.github.mimoguz.pixelj.models.ProjectModel;
import io.github.mimoguz.pixelj.views.shared.Components;
import io.github.mimoguz.pixelj.views.shared.Dimensions;

public class InfoPanel extends JPanel {
    private transient CharacterModel model;
    private final JLabel glyph;
    private final JLabel name;
    private final JLabel codePoint;
    private final JSpinner characterWidth;

    public InfoPanel(final ProjectModel project) {
        glyph = new JLabel(" ");
        glyph.setFont(glyph.getFont().deriveFont(Font.PLAIN, 48));

        name = new JLabel(" ");

        codePoint = new JLabel(" ");
        codePoint.putClientProperty(FlatClientProperties.STYLE_CLASS, "small");

        characterWidth = new JSpinner();
        Components.setFixedSize(characterWidth, Dimensions.SPINNER_SIZE);

        characterWidth.addChangeListener(e -> {
            if (model != null && characterWidth.getModel() instanceof SpinnerNumberModel numberModel) {
                final var value = numberModel.getNumber().intValue();
                if (value != model.getWidth()) {
                    model.setWidth(value);
                }
            }
        });

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        add(glyph);
        add(name);
        add(codePoint);
        add(characterWidth);

        setMetrics(project.getMetrics());
    }

    public CharacterModel getModel() {
        return model;
    }

    public void setModel(final CharacterModel value) {
        this.model = value;
        if (value != null) {
            name.setText("CHARACTER NAME");
            codePoint.setText(Integer.toString(value.getCodePoint()));
            glyph.setText(Character.toString((char) model.getCodePoint()));
            if (characterWidth.getModel() instanceof SpinnerNumberModel numberModel) {
                numberModel.setValue(value.getWidth());
            }
        } else {
            name.setText(" ");
            codePoint.setText(" ");
            glyph.setText(" ");
        }
    }

    public void setMetrics(final Metrics metrics) {
        characterWidth.setModel(
                new SpinnerNumberModel(
                        model != null ? model.getWidth() : metrics.defaultCharacterWidth(),
                        0,
                        metrics.canvasWidth(),
                        1
                )
        );
    }
}
