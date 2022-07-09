package pixelj.views.glyphs_screen;

import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;

import com.formdev.flatlaf.FlatClientProperties;

import pixelj.models.Project;
import pixelj.resources.Resources;
import pixelj.views.controls.GlyphPainter;
import pixelj.views.controls.ZoomStrip;
import pixelj.views.shared.Borders;
import pixelj.views.shared.Dimensions;

/**
 * PainterPanel design.
 */
public abstract class PainterPanelBase extends JPanel {
    protected final GlyphPainter painter = new GlyphPainter(Resources.get().colors.disabledIcon());
    protected final ZoomStrip zoomStrip = new ZoomStrip(1, 48, 12);
    protected final InfoPanel infoPanel;
    protected final JToolBar toolBar = new JToolBar();

    public PainterPanelBase(
                final Project project,
                final JComponent root,
                final InfoPanel infoPanel
    ) {
        this.infoPanel = infoPanel;

        setLayout(new BorderLayout());
        add(infoPanel, BorderLayout.EAST);

        final var editorPanel = new JPanel(new BorderLayout());
        editorPanel.setBorder(
                BorderFactory.createMatteBorder(0, 0, 0, 1, Resources.get().colors.divider())
        );
        editorPanel.add(zoomStrip, BorderLayout.SOUTH);
        editorPanel.add(toolBar, BorderLayout.WEST);

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
        final var painterContainer = new JPanel();
        painterContainer.setLayout(new GridBagLayout());
        painterContainer.setMaximumSize(Dimensions.MAXIMUM);
        painterContainer.add(painter);
        final var scrollPanel = new JScrollPane(painterContainer);
        scrollPanel.setBorder(Borders.SMALL_EMPTY_CUP_CENTER);
        scrollPanel.setFocusable(true);
        scrollPanel.setMaximumSize(Dimensions.MAXIMUM);
        // When clicked, move focus to scrollPanel
        final var moveFocus = new MouseAdapter() {
            @Override
            public void mousePressed(final MouseEvent e) {
                scrollPanel.requestFocus();
            }
        };
        scrollPanel.addMouseListener(moveFocus);
        painter.addMouseListener(moveFocus);
        editorPanel.add(scrollPanel, BorderLayout.CENTER);

        add(editorPanel, BorderLayout.CENTER);
    }
}
