package pixelj.views.projectwindow.glyphspage;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;

import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SwingConstants;

import com.formdev.flatlaf.FlatClientProperties;

import pixelj.resources.Resources;
import pixelj.views.shared.Components;
import pixelj.views.shared.Dimensions;

/** InfoPanel design. */
public abstract class InfoPanelBase extends JPanel {
    /** Blank string. */
    protected static final String BLANK = " ";
    /** Subtitle class property. */
    protected static final String SUBTITLE_STYLE = "small";
    /** Glyph reference font size. */
    protected static final int GLYPH_LABEL_SIZE = 120;

    private static final Color LABEL_FOREGROUND = new Color(50, 55, 65);

    /** Glyph width input. */
    protected final JSpinner widthSpinner = new JSpinner();
    /** Glyph reference. */
    protected final JLabel glyphLabel = new JLabel(BLANK);
    /** Glyph name. */
    protected final JLabel nameLabel = new JLabel(BLANK);
    /** Code point. */
    protected final JLabel codePointLabel = new JLabel(BLANK);
    /** Block name. */
    protected final JLabel blockNameLabel = new JLabel(BLANK);
    /** Width spinner label. */
    protected final JLabel widthLabel = new JLabel(Resources.get().getString("widthSpinnerLabel"));
    /** Grid toggle. */
    protected final JCheckBox showGridCheckBox = new JCheckBox(Resources.get().getString("showGrid"));
    /** Guide toggle. */
    protected final JCheckBox showGuidesCheckBox = new JCheckBox(Resources.get().getString("showGuides"));

    public InfoPanelBase() {
        glyphLabel.setHorizontalAlignment(SwingConstants.CENTER);
        glyphLabel.setForeground(LABEL_FOREGROUND);
        codePointLabel.putClientProperty(FlatClientProperties.STYLE_CLASS, SUBTITLE_STYLE);
        blockNameLabel.putClientProperty(FlatClientProperties.STYLE_CLASS, SUBTITLE_STYLE);
        Components.setFixedSize(widthSpinner, Dimensions.SPINNER_SIZE);

        final var pad = Dimensions.MEDIUM_PADDING;
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
        final var glyphBackground = new JPanel(new GridLayout());
        // noinspection SuspiciousNameCombination
        Components.setFixedSize(glyphBackground, new Dimension(innerWidth, innerWidth));
        glyphBackground.add(glyphLabel);
        glyphBackground.setBackground(Color.WHITE);
        add(glyphBackground, cons);

        cons.gridy = 1;
        cons.insets = new Insets(0, pad, pad, pad - divWidth);
        final var titlePanel = new JPanel();
        Components.setFixedSize(titlePanel, new Dimension(innerWidth, 80));
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.add(nameLabel);
        titlePanel.add(codePointLabel);
        titlePanel.add(blockNameLabel);
        add(titlePanel, cons);

        cons.gridy = 2;
        cons.gridwidth = 1;
        cons.gridx = 0;
        cons.insets = new Insets(pad, pad, pad, pad);
        add(widthLabel, cons);

        cons.gridx = 1;
        cons.insets = new Insets(pad, 0, pad, pad - divWidth - focusWidth);
        cons.anchor = GridBagConstraints.EAST;
        add(widthSpinner, cons);

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
        add(showGuidesCheckBox, cons);
    }
}
