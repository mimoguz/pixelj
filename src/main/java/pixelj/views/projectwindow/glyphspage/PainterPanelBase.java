package pixelj.views.projectwindow.glyphspage;

import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.*;
import javax.swing.border.Border;

import com.formdev.flatlaf.FlatClientProperties;

import pixelj.resources.Resources;
import pixelj.views.controls.GlyphPainter;
import pixelj.views.controls.ZoomStrip;
import pixelj.views.shared.Borders;
import pixelj.views.shared.Components;
import pixelj.views.shared.Dimensions;
import pixelj.views.shared.VerticalFlowLayout;

/**
 * PainterPanel design.
 */
public abstract class PainterPanelBase extends JPanel {

    /**
     * Focus border width.
     */
    private static final int BW = 4;

    protected final GlyphPainter painter = new GlyphPainter(Resources.get().colors.disabledIcon());
    protected final ZoomStrip zoomStrip = new ZoomStrip(1, 48, 12);
    protected final InfoPanel infoPanel;
    protected final JToolBar toolBar = new JToolBar();
    protected final JScrollPane scrollPane;

    private final Border focusedBorder = BorderFactory.createLineBorder(Resources.get().colors.accent(), BW);
    private final Border unfocusedBorder = BorderFactory.createEmptyBorder(BW, BW, BW, BW);

    public PainterPanelBase(final InfoPanel infoPanel) {
        this.infoPanel = infoPanel;

        toolBar.setBorder(Borders.MEDIUM_EMPTY_CUP);
        Components.addOuterBorder(toolBar,
            BorderFactory.createMatteBorder(0, 0, 0, 1, Resources.get().colors.separator())
        );
        toolBar.setOrientation(SwingConstants.VERTICAL);
        toolBar.setLayout(new VerticalFlowLayout());

        setLayout(new BorderLayout());
        add(infoPanel, BorderLayout.EAST);

        /* ------------------------------- Panel title ------------------------------ */
        final var title = new JLabel(Resources.get().getString("painterTitle"));
        title.putClientProperty(FlatClientProperties.STYLE_CLASS, "h3");
        final var titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.X_AXIS));
        titlePanel.setBorder(Borders.TITLE);
        titlePanel.add(Box.createHorizontalStrut(Dimensions.LARGE_PADDING));
        titlePanel.add(title);
        titlePanel.add(Box.createHorizontalGlue());

        /* --------------------------------- Canvas --------------------------------- */
        // TODO: Focus border is a temporary solution. I need to make shortcut keys always work.
        final var painterBorder = new JPanel(new GridLayout(1, 1));
        painterBorder.setBorder(unfocusedBorder);
        painterBorder.add(painter);
        final var painterContainer = new JPanel();
        painterContainer.setLayout(new GridBagLayout());
        painterContainer.setMaximumSize(Dimensions.MAXIMUM);
        painterContainer.add(painterBorder);

        scrollPane = new JScrollPane(painterContainer);
        //scrollPane.setBorder(Borders.SMALL_EMPTY_CUP_CENTER);
        scrollPane.setBorder(null);
        scrollPane.setFocusable(true);
        scrollPane.setMaximumSize(Dimensions.MAXIMUM);

        final var editorPanel = new JPanel(new BorderLayout());
        editorPanel.add(scrollPane, BorderLayout.CENTER);
        editorPanel.add(zoomStrip, BorderLayout.SOUTH);

        final var centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, Resources.get().colors.separator()));
        centerPanel.add(titlePanel, BorderLayout.NORTH);
        centerPanel.add(toolBar, BorderLayout.WEST);
        centerPanel.add(editorPanel, BorderLayout.CENTER);
        add(centerPanel, BorderLayout.CENTER);

        scrollPane.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(final FocusEvent e) {
                painterBorder.setBorder(focusedBorder);
            }

            @Override
            public void focusLost(final FocusEvent e) {
                painterBorder.setBorder(unfocusedBorder);
            }
        });
    }
}
