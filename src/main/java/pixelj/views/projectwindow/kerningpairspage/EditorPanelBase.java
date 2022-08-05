package pixelj.views.projectwindow.kerningpairspage;

import java.awt.GridBagLayout;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;

import com.formdev.flatlaf.FlatClientProperties;

import pixelj.resources.Resources;
import pixelj.views.controls.StringView;
import pixelj.views.controls.ZoomStrip;
import pixelj.views.shared.Borders;
import pixelj.views.shared.Components;
import pixelj.views.shared.Dimensions;

/** Editor panel design. */
abstract class EditorPanelBase extends JPanel {

    protected static final int INITIAL_ZOOM = 4;

    protected final JLabel pxLabel = new JLabel(Resources.get().getString("pixels"));
    protected final JLabel spinnerLabel = new JLabel(Resources.get().getString("kerningValue"));
    protected final JScrollPane scrollPane;
    protected final JSpinner valueSpinner = new JSpinner();
    protected final StringView preview = new StringView(Resources.get().colors.disabledIcon());
    protected final ZoomStrip zoomStrip = new ZoomStrip(1, 48, INITIAL_ZOOM);

    EditorPanelBase() {
        final var res = Resources.get();

        preview.setPadding(Dimensions.MEDIUM_PADDING);
        preview.setZoom(INITIAL_ZOOM);

        Components.setFixedSize(valueSpinner, Dimensions.SPINNER_SIZE);
        valueSpinner.setAlignmentY(0.5f);
        spinnerLabel.setAlignmentY(0.5f);
        pxLabel.setAlignmentY(0.5f);

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        final var title = new JLabel(res.getString("kerningValueEditorTitle"));
        title.putClientProperty(FlatClientProperties.STYLE_CLASS, "h3");
        final var titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.X_AXIS));
        titlePanel.setBorder(Borders.TITLE_CENTER);
        titlePanel.add(Box.createHorizontalStrut(Dimensions.LARGE_PADDING));
        titlePanel.add(title);
        titlePanel.add(Box.createHorizontalGlue());
        add(titlePanel);

        final var previewPanel = new JPanel(new GridBagLayout());
        previewPanel.add(preview);

        scrollPane = new JScrollPane(previewPanel);
        // To balance the split pane divider
        scrollPane.setBorder(Borders.SMALL_EMPTY_CUP_CENTER);
        scrollPane.setMaximumSize(Dimensions.MAXIMUM);
        add(scrollPane);

        final var spinnerPanel = new JPanel();
        spinnerPanel.setLayout(new BoxLayout(spinnerPanel, BoxLayout.X_AXIS));
        spinnerPanel.setBorder(Borders.SMALL_EMPTY_BOTTOM_CENTER_PANEL);
        spinnerPanel.add(Box.createHorizontalGlue());
        spinnerPanel.add(spinnerLabel);
        spinnerPanel.add(Box.createRigidArea(Dimensions.SMALL_SQUARE));
        spinnerPanel.add(valueSpinner);
        spinnerPanel.add(Box.createRigidArea(Dimensions.SMALL_SQUARE));
        spinnerPanel.add(pxLabel);
        spinnerPanel.add(Box.createHorizontalGlue());
        add(spinnerPanel);

        add(zoomStrip);
    }
}
