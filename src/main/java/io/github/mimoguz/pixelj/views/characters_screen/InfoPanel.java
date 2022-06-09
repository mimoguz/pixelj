package io.github.mimoguz.pixelj.views.characters_screen;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import com.formdev.flatlaf.FlatClientProperties;

import io.github.mimoguz.pixelj.models.CharacterItem;
import io.github.mimoguz.pixelj.models.Metrics;
import io.github.mimoguz.pixelj.models.Project;
import io.github.mimoguz.pixelj.resources.Resources;
import io.github.mimoguz.pixelj.views.shared.Components;
import io.github.mimoguz.pixelj.views.shared.Dimensions;

public class InfoPanel extends JPanel {
    private static final Color LABEL_FOREGROUND = new Color(50, 55, 65);

    private final JLabel characterWidthLabel;
    private final JSpinner characterWidthSpinner;
    private final JLabel codePointLabel;
    private final JLabel blockNamePanel;
    private final JLabel glyphLabel;
    private CharacterItem model;
    private final JLabel nameLabel;
    private final JCheckBox showGridCheckBox;
    private final JCheckBox showLinesCheckBox;

    public InfoPanel(final Project project) {
        final var res = Resources.get();

        glyphLabel = new JLabel(" ");
        glyphLabel.setFont(glyphLabel.getFont().deriveFont(Font.PLAIN, 120));
        glyphLabel.setForeground(LABEL_FOREGROUND);

        nameLabel = new JLabel(" ");

        codePointLabel = new JLabel(" ");
        codePointLabel.putClientProperty(FlatClientProperties.STYLE_CLASS, "small");

        blockNamePanel = new JLabel(" ");
        blockNamePanel.putClientProperty(FlatClientProperties.STYLE_CLASS, "small");

        characterWidthSpinner = new JSpinner();
        Components.setFixedSize(characterWidthSpinner, Dimensions.SPINNER_SIZE);
        characterWidthSpinner.setEnabled(false);

        characterWidthLabel = new JLabel(res.getString("characterWidthSpinnerLabel"));
        characterWidthLabel.setEnabled(false);

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

        final var pad = Dimensions.MEDIUM_SQUARE.width;
        final var focusWidth = 2;
        final var divWidth = 5;
        final var panelWidth = 212;
        final var cons = new GridBagConstraints();
        final var innerWidth = panelWidth - 2 * pad + divWidth;

        setLayout(new GridBagLayout());
        setMaximumSize(new Dimension(panelWidth, Integer.MAX_VALUE));
        setMinimumSize(new Dimension(panelWidth, 1));
        setPreferredSize(new Dimension(panelWidth, 400));

        cons.gridy = 0;
        cons.gridwidth = 2;
        cons.weighty = 0.0;
        cons.insets = new Insets(pad - focusWidth, pad, pad, pad - divWidth);
        final var glyphBackground = new JPanel(new GridBagLayout());
        //noinspection SuspiciousNameCombination
        Components.setFixedSize(glyphBackground, new Dimension(innerWidth, innerWidth));
        glyphBackground.add(glyphLabel, new GridBagConstraints());
        glyphBackground.setBackground(Color.WHITE);
        add(glyphBackground, cons);

        cons.gridy = 1;
        cons.insets = new Insets(pad, pad, pad, pad - divWidth);
        final var titlePanel = new JPanel();
        Components.setFixedSize(titlePanel, new Dimension(innerWidth, 96));
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.add(nameLabel);
        titlePanel.add(codePointLabel);
        titlePanel.add(blockNamePanel);
        add(titlePanel, cons);

        cons.gridy = 2;
        cons.gridwidth = 1;
        cons.gridx = 0;
        cons.insets = new Insets(pad, pad, pad, pad);
        add(characterWidthLabel, cons);

        cons.gridx = 1;
        cons.insets = new Insets(pad, 0, pad, pad - divWidth - focusWidth);
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
        cons.insets = new Insets(pad, pad - focusWidth, pad, pad - divWidth);
        add(showGridCheckBox, cons);

        cons.gridy = 5;
        cons.insets = new Insets(0, pad - focusWidth, pad * 2, pad - divWidth);
        add(showLinesCheckBox, cons);

        setMetrics(project.getMetrics());
    }

    public CharacterItem getModel() {
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

    public void setModel(final CharacterItem value) {
        this.model = value;
        if (value != null) {
            final var res = Resources.get();
            final var characterData = res.getCharacterData(value.getCodePoint());
            final var name = String.format(
                    "<html><body style=\"text-align: left; \">%s</body></html>",
                    characterData.name()
            );
            nameLabel.setText(name);
            codePointLabel.setText(
                    res.formatString("codePointLabel", Integer.toHexString(characterData.codePoint()))
            );
            blockNamePanel.setText(
                    res.formatString("blockNameLabel", res.getBlockData(characterData.blockId()).name())
            );
            glyphLabel.setText(Character.toString((char) model.getCodePoint()));
            if (characterWidthSpinner.getModel() instanceof SpinnerNumberModel numberModel) {
                numberModel.setValue(value.getWidth());
                characterWidthSpinner.setEnabled(true);
                characterWidthLabel.setEnabled(true);
            }
        } else {
            nameLabel.setText(" ");
            codePointLabel.setText(" ");
            blockNamePanel.setText(" ");
            glyphLabel.setText(" ");
            characterWidthSpinner.setEnabled(false);
            characterWidthLabel.setEnabled(false);
        }
    }
}
