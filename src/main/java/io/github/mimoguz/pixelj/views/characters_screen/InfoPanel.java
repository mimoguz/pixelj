package io.github.mimoguz.pixelj.views.characters_screen;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.*;

import com.formdev.flatlaf.FlatClientProperties;

import io.github.mimoguz.pixelj.models.CharacterModel;
import io.github.mimoguz.pixelj.models.Metrics;
import io.github.mimoguz.pixelj.models.ProjectModel;
import io.github.mimoguz.pixelj.resources.Resources;
import io.github.mimoguz.pixelj.views.shared.Components;
import io.github.mimoguz.pixelj.views.shared.Dimensions;

public class InfoPanel extends JPanel {
    private final JSpinner characterWidthSpinner;
    private final JLabel codePointLabel;
    private final JLabel glyphLabel;
    private transient CharacterModel model;
    private final JLabel nameLabel;
    private final JCheckBox showGridCheckBox;
    private final JCheckBox showLinesCheckBox;

    public InfoPanel(final ProjectModel project) {
        final var res = Resources.get();

        glyphLabel = new JLabel(" ");
        glyphLabel.setFont(glyphLabel.getFont().deriveFont(Font.PLAIN, 102));

        nameLabel = new JLabel(" ");

        codePointLabel = new JLabel(" ");
        codePointLabel.putClientProperty(FlatClientProperties.STYLE_CLASS, "small");

        characterWidthSpinner = new JSpinner();
        Components.setFixedSize(characterWidthSpinner, Dimensions.SPINNER_SIZE);

        showGridCheckBox = new JCheckBox(res.getString("showGrid"));
        showGridCheckBox.setSelected(true);

        showLinesCheckBox = new JCheckBox(res.getString("showGuides"));
        showLinesCheckBox.setSelected(true);

        characterWidthSpinner.addChangeListener(e -> {
            if (model != null && characterWidthSpinner.getModel() instanceof SpinnerNumberModel numberModel) {
                final var value = numberModel.getNumber().intValue();
                if (value != model.getWidth()) {
                    model.setWidth(value);
                }
            }
        });

        final var pad = 4;
        final var panelWidth = 200;
        final var cons = new GridBagConstraints();

        setLayout(new GridBagLayout());
        setBorder(BorderFactory.createEmptyBorder(pad, pad, pad, 0));
        setMaximumSize(new Dimension(panelWidth, Integer.MAX_VALUE));
        setPreferredSize(new Dimension(panelWidth, 400));

        cons.gridy = 0;
        cons.gridwidth = 2;
        cons.weighty = 0.0;
        final var glyphBackground = new JPanel(new GridBagLayout());
        Components.setFixedSize(glyphBackground, new Dimension(panelWidth - pad, panelWidth - pad));
        glyphBackground.add(glyphLabel, new GridBagConstraints());
        glyphBackground.setBackground(Resources.get().colors.box());
        add(glyphBackground, cons);

        cons.gridy = 1;
        final var titlePanel = new JPanel();
        Components.setFixedSize(titlePanel, new Dimension(panelWidth - pad, 96));
        titlePanel.setBorder(BorderFactory.createEmptyBorder(pad, pad, pad, pad));
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.add(nameLabel);
        titlePanel.add(codePointLabel);
        add(titlePanel, cons);

        cons.gridy = 2;
        cons.gridwidth = 1;
        cons.gridx = 0;
        add(new JLabel(res.getString("characterWidthSpinnerLabel")), cons);

        cons.gridx = 1;
        cons.anchor = GridBagConstraints.EAST;
        add(characterWidthSpinner, cons);

        cons.gridy = 3;
        cons.weighty = 1.0;
        cons.gridwidth = 2;
        add(new JPanel(), cons);

        cons.gridy = 4;
        cons.gridx = 0;
        cons.weighty = 0.0;
        cons.anchor = GridBagConstraints.WEST;
        add(showGridCheckBox, cons);

        cons.gridy = 5;
        add(Box.createRigidArea(Dimensions.MEDIUM_SQUARE), cons);

        cons.gridy = 6;
        add(showLinesCheckBox, cons);

        setMetrics(project.getMetrics());
    }

    public CharacterModel getModel() {
        return model;
    }

    public JCheckBox getShowGridCheckBox() {
        return showGridCheckBox;
    }

    public JCheckBox getShowLinesCheckBox() {
        return showLinesCheckBox;
    }

    public void setMetrics(final Metrics metrics) {
        characterWidthSpinner.setModel(
                new SpinnerNumberModel(
                        model != null ? model.getWidth() : metrics.defaultCharacterWidth(),
                        0,
                        metrics.canvasWidth(),
                        1
                )
        );
    }

    public void setModel(final CharacterModel value) {
        this.model = value;
        if (value != null) {
            nameLabel.setText("CHARACTER NAME");
            codePointLabel.setText(Integer.toString(value.getCodePoint()));
            glyphLabel.setText(Character.toString((char) model.getCodePoint()));
            if (characterWidthSpinner.getModel() instanceof SpinnerNumberModel numberModel) {
                numberModel.setValue(value.getWidth());
            }
        } else {
            nameLabel.setText(" ");
            codePointLabel.setText(" ");
            glyphLabel.setText(" ");
        }
    }
}
