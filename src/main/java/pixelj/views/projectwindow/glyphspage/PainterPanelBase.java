package pixelj.views.projectwindow.glyphspage;

import java.awt.BorderLayout;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.GridBagLayout;
import java.awt.GridLayout;

import javax.swing.border.Border;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;

import com.formdev.flatlaf.FlatClientProperties;

import pixelj.resources.Resources;
import pixelj.views.controls.GlyphPainter;
import pixelj.views.controls.ZoomStrip;
import pixelj.views.shared.Borders;
import pixelj.views.shared.Dimensions;
import pixelj.views.shared.ToolLayout;

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
    protected final JButton overflowButton = new JButton();
    protected final JScrollPane scrollPane;

    private final Border focusedBorder = BorderFactory.createLineBorder(Resources.get().colors.accent(), BW);
    private final Border unfocusedBorder = BorderFactory.createEmptyBorder(BW, BW, BW, BW);

    public PainterPanelBase(final InfoPanel infoPanel) {
        this.infoPanel = infoPanel;

        setLayout(new BorderLayout());
        add(infoPanel, BorderLayout.EAST);

        final var editorPanel = new JPanel(new BorderLayout());
        editorPanel.setBorder(
            BorderFactory.createMatteBorder(0, 0, 0, 1, Resources.get().colors.divider())
        );
        editorPanel.add(zoomStrip, BorderLayout.SOUTH);

        final var toolsPanel = new JPanel(new BorderLayout());
        toolsPanel.add(toolBar, BorderLayout.CENTER);
        overflowButton.putClientProperty(
            FlatClientProperties.BUTTON_TYPE,
            FlatClientProperties.BUTTON_TYPE_TOOLBAR_BUTTON
        );
        overflowButton.setFocusable(false);
        toolsPanel.add(overflowButton, BorderLayout.SOUTH);

        editorPanel.add(toolsPanel, BorderLayout.WEST);

        toolBar.setOrientation(SwingConstants.VERTICAL);
        toolBar.setBorder(Borders.SMALL_EMPTY_CUP);

        /* ------------------------------- Panel title ------------------------------ */
        final var title = new JLabel(Resources.get().getString("painterTitle"));
        title.putClientProperty(FlatClientProperties.STYLE_CLASS, "h3");
        final var titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.X_AXIS));
        titlePanel.setBorder(Borders.TITLE_CENTER);
        titlePanel.add(Box.createHorizontalStrut(Dimensions.LARGE_PADDING));
        titlePanel.add(title);
        titlePanel.add(Box.createHorizontalGlue());
        editorPanel.add(titlePanel, BorderLayout.NORTH);

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
        scrollPane.setBorder(Borders.SMALL_EMPTY_CUP_CENTER);
        scrollPane.setFocusable(true);
        scrollPane.setMaximumSize(Dimensions.MAXIMUM);
        editorPanel.add(scrollPane, BorderLayout.CENTER);

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

        add(editorPanel, BorderLayout.CENTER);
    }
}
