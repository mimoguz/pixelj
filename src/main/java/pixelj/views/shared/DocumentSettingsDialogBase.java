package pixelj.views.shared;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextField;

import com.formdev.flatlaf.FlatClientProperties;

import pixelj.resources.Resources;

/** Document settings dialog design. */
abstract class DocumentSettingsDialogBase extends JDialog {

    protected final JButton applyButton;
    protected final JButton cancelButton = new JButton(Resources.get().getString("cancel"));
    protected final JButton helpButton = new JButton();

    protected final JTextField titleIn = new JTextField();
    protected final JSpinner ascenderIn = new JSpinner();
    protected final JSpinner canvasHeightIn = new JSpinner();
    protected final JSpinner canvasWidthIn = new JSpinner();
    protected final JSpinner capHeightIn = new JSpinner();
    protected final JSpinner defaultWidthIn = new JSpinner();
    protected final JSpinner descenderIn = new JSpinner();
    protected final JSpinner lineSpacingIn = new JSpinner();
    protected final JSpinner spaceSizeIn = new JSpinner();
    protected final JSpinner letterSpacingIn = new JSpinner();
    protected final JSpinner xHeightIn = new JSpinner();
    protected final JCheckBox isBoldIn = new JCheckBox();
    protected final JCheckBox isItalicIn = new JCheckBox();
    protected final JCheckBox isMonospacedIn = new JCheckBox();

    DocumentSettingsDialogBase(final Frame owner, final String dialogTitle, final String applyButtonLabel) {
        super(owner, dialogTitle, Dialog.ModalityType.APPLICATION_MODAL);

        applyButton = new JButton(applyButtonLabel);
        Components.setFixedSize(applyButton, Dimensions.TEXT_BUTTON_SIZE);
        Components.setFixedSize(cancelButton, Dimensions.TEXT_BUTTON_SIZE);
        helpButton.putClientProperty(
                FlatClientProperties.BUTTON_TYPE,
                FlatClientProperties.BUTTON_TYPE_BORDERLESS
        );

        final var content = new JPanel(
                new BorderLayout(Dimensions.MEDIUM_PADDING, Dimensions.LARGE_PADDING * 2)
        );
        content.setBorder(Borders.LARGE_EMPTY);

        final var metricsPanel = makeMetricsPanel();
        addTitlePanel(content);
        addMetricsPanel(content, metricsPanel);
        addButtons(content);

        setContentPane(content);
        getRootPane().setDefaultButton(applyButton);
        getRootPane().putClientProperty(FlatClientProperties.TITLE_BAR_SHOW_ICON, false);
        pack();
        metricsPanel.doLayout();
        setSize(300, 680);
        setResizable(false);
    }

    @Override
    public void setVisible(final boolean visible) {
        if (visible) {
            setLocationRelativeTo(getOwner());
        }
        super.setVisible(visible);
    }

    private JPanel makeMetricsPanel() {
        final var panel = new JPanel(new GridBagLayout());
        panel.setBorder(Borders.EMPTY);
        final var cons = new GridBagConstraints();
        final var res = Resources.get();

        Components.setFixedSize(ascenderIn, Dimensions.SPINNER_SIZE);
        Components.setFixedSize(canvasHeightIn, Dimensions.SPINNER_SIZE);
        Components.setFixedSize(canvasWidthIn, Dimensions.SPINNER_SIZE);
        Components.setFixedSize(capHeightIn, Dimensions.SPINNER_SIZE);
        Components.setFixedSize(defaultWidthIn, Dimensions.SPINNER_SIZE);
        Components.setFixedSize(descenderIn, Dimensions.SPINNER_SIZE);
        Components.setFixedSize(lineSpacingIn, Dimensions.SPINNER_SIZE);
        Components.setFixedSize(spaceSizeIn, Dimensions.SPINNER_SIZE);
        Components.setFixedSize(letterSpacingIn, Dimensions.SPINNER_SIZE);
        Components.setFixedSize(xHeightIn, Dimensions.SPINNER_SIZE);

        /* --------------------------------- Labels --------------------------------- */
        cons.gridx = 0;
        cons.gridy = 0;
        cons.weightx = 1.0;
        cons.insets = new Insets(0, 0, Dimensions.SMALL_PADDING, Dimensions.MEDIUM_PADDING);
        cons.anchor = GridBagConstraints.LINE_START;
        panel.add(new JLabel(res.getString("documentCanvasWidth")), cons);
        cons.gridy = GridBagConstraints.RELATIVE;
        panel.add(new JLabel(res.getString("documentCanvasHeight")), cons);
        panel.add(new JLabel(res.getString("documentAscender")), cons);
        panel.add(new JLabel(res.getString("documentDescender")), cons);
        panel.add(new JLabel(res.getString("documentCapHeight")), cons);
        panel.add(new JLabel(res.getString("documentXHeight")), cons);
        panel.add(new JLabel(res.getString("documentDefaultWidth")), cons);
        panel.add(new JLabel(res.getString("documentLetterSpacing")), cons);
        panel.add(new JLabel(res.getString("documentSpaceSize")), cons);
        panel.add(new JLabel(res.getString("documentLineSpacing")), cons);
        final var labelBorder = BorderFactory.createEmptyBorder(8, 0, 8, 0);
        final var isMonospacedLabel = new JLabel(res.getString("documentIsMonospaced"));
        isMonospacedLabel.setBorder(labelBorder);
        panel.add(isMonospacedLabel, cons);
        final var isBoldLabel = new JLabel(res.getString("documentIsBold"));
        isBoldLabel.setBorder(labelBorder);
        panel.add(isBoldLabel, cons);
        final var isItalicLabel = new JLabel(res.getString("documentIsItalic"));
        isItalicLabel.setBorder(labelBorder);
        panel.add(isItalicLabel, cons);

        /* --------------------------------- Inputs --------------------------------- */
        cons.gridx = 1;
        cons.gridy = 0;
        cons.weightx = 0.0;
        cons.insets = new Insets(0, 0, Dimensions.SMALL_PADDING, 0);
        cons.fill = GridBagConstraints.NONE;
        cons.anchor = GridBagConstraints.LINE_END;
        panel.add(canvasWidthIn, cons);
        cons.gridy = GridBagConstraints.RELATIVE;
        panel.add(canvasHeightIn, cons);
        panel.add(ascenderIn, cons);
        panel.add(descenderIn, cons);
        panel.add(capHeightIn, cons);
        panel.add(xHeightIn, cons);
        panel.add(defaultWidthIn, cons);
        panel.add(letterSpacingIn, cons);
        panel.add(spaceSizeIn, cons);
        panel.add(lineSpacingIn, cons);
        panel.add(isMonospacedIn, cons);
        panel.add(isBoldIn, cons);
        panel.add(isItalicIn, cons);

        return panel;
    }

    private void addTitlePanel(final JPanel content) {
        final var titlePanel = new JPanel(new BorderLayout(0, Dimensions.MEDIUM_PADDING));
        final var titleLabel = new JLabel(Resources.get().getString("projectSectionTitle"));
        styleTitle(titleLabel);
        titlePanel.add(titleLabel, BorderLayout.NORTH);
        titlePanel.add(titleIn, BorderLayout.CENTER);
        content.add(titlePanel, BorderLayout.NORTH);
    }

    private void addMetricsPanel(final JPanel content, final JPanel metricsPanel) {
        final var centerPanel = new JPanel(new BorderLayout(0, Dimensions.MEDIUM_PADDING));
        final var metricsLabel = new JLabel(Resources.get().getString("metricsSectionTitle"));
        styleTitle(metricsLabel);
        centerPanel.add(metricsLabel, BorderLayout.NORTH);

        final var scroll = new JScrollPane(metricsPanel);
        scroll.setBorder(Borders.EMPTY);
        centerPanel.add(scroll, BorderLayout.CENTER);
        content.add(centerPanel, BorderLayout.CENTER);
    }

    private void addButtons(final JPanel content) {
        final var buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.add(helpButton);
        buttonPanel.add(Box.createRigidArea(Dimensions.MEDIUM_SQUARE));
        buttonPanel.add(Box.createHorizontalGlue());
        buttonPanel.add(applyButton);
        buttonPanel.add(Box.createRigidArea(Dimensions.MEDIUM_SQUARE));
        buttonPanel.add(cancelButton);
        buttonPanel.setBorder(Borders.EMPTY);
        content.add(buttonPanel, BorderLayout.SOUTH);
    }

    private void styleTitle(final JLabel title) {
        title.setBorder(BorderFactory.createEmptyBorder(0, 0, Dimensions.SMALL_PADDING, 0));
        Components.addOuterBorder(
                title,
                BorderFactory.createMatteBorder(0, 0, 1, 0, Resources.get().colors.divider())
        );
        title.putClientProperty(FlatClientProperties.STYLE_CLASS, "h4");
    }
}
