package pixelj.views.preview_screen;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;

import com.formdev.flatlaf.FlatClientProperties;

import pixelj.actions.Actions;
import pixelj.actions.ApplicationAction;
import pixelj.actions.PreviewScreenActions;
import pixelj.models.Project;
import pixelj.resources.Icons;
import pixelj.resources.Resources;
import pixelj.util.Detachable;
import pixelj.views.controls.PromptTextArea;
import pixelj.views.controls.ZoomStrip;
import pixelj.views.shared.Borders;
import pixelj.views.shared.Components;
import pixelj.views.shared.Dimensions;

public final class PreviewScreen extends JPanel implements Detachable {
    private static final int INITIAL_ZOOM = 4;
    private final PreviewScreenActions actions;
    private final PromptTextArea textInput = new PromptTextArea();
    private final ZoomStrip zoomStrip = new ZoomStrip(1, 48, INITIAL_ZOOM);

    public PreviewScreen(final Project project, final JComponent root) {
        final var res = Resources.get();

        textInput.setPromptColor(res.colors.inactive());
        textInput.setMaximumSize(Dimensions.MAXIMUM);
        textInput.setPromptText(res.getString("previewTextInputPrompt"));

        final var contextMenu = makeContextMenu();
        textInput.setComponentPopupMenu(contextMenu);

        final var container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        container.setBorder(
                BorderFactory.createMatteBorder(
                        Dimensions.MEDIUM_PADDING,
                        Dimensions.MEDIUM_PADDING,
                        Dimensions.MEDIUM_PADDING,
                        Dimensions.MEDIUM_PADDING,
                        Color.WHITE
                )
        );
        container.setBackground(Color.WHITE);

        actions = new PreviewScreenActions(project, textInput, container);
        actions.setZoom(INITIAL_ZOOM);
        Actions.registerShortcuts(actions.all, root);

        final var refreshButton = new JButton();
        refreshButton.setAction(actions.refreshAction);
        Components.setFixedSize(refreshButton, Dimensions.TEXT_BUTTON_SIZE);

        final var clearButton = new JButton();
        clearButton.setAction(actions.clearAction);
        Components.setFixedSize(clearButton, Dimensions.TEXT_BUTTON_SIZE);

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        final var title = new JLabel(res.getString("previewTitle"));
        title.putClientProperty(FlatClientProperties.STYLE_CLASS, "h3");
        final var titlePanel = new JPanel();
        titlePanel.setBorder(Borders.TITLE_CENTER);
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.X_AXIS));
        titlePanel.add(Box.createHorizontalStrut(Dimensions.LARGE_PADDING));
        titlePanel.add(title);
        titlePanel.add(Box.createHorizontalGlue());
        add(titlePanel);

        final var previewPanel = new JPanel(new GridBagLayout());
        previewPanel.add(container);
        final var scrollPanel = new JScrollPane(previewPanel);
        scrollPanel.setMaximumSize(Dimensions.MAXIMUM);
        scrollPanel.setBorder(Borders.SMALL_EMPTY_CENTER_PANEL);
        add(scrollPanel);

        final var inputPanel = new JPanel();
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));
        inputPanel.setMinimumSize(new Dimension(200, 144));
        inputPanel.setPreferredSize(new Dimension(600, 144));
        inputPanel.setMaximumSize(new Dimension(800, 144));
        inputPanel.add(textInput);
        inputPanel.add(Box.createRigidArea(Dimensions.SMALL_SQUARE));
        final var buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.add(Box.createHorizontalGlue());
        buttonPanel.add(refreshButton);
        buttonPanel.add(Box.createRigidArea(Dimensions.SMALL_SQUARE));
        buttonPanel.add(clearButton);
        inputPanel.add(buttonPanel);
        final var bottomPanel = new JPanel();
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.X_AXIS));
        bottomPanel.setBorder(Borders.SMALL_EMPTY_BOTTOM_CENTER_PANEL);
        bottomPanel.add(Box.createHorizontalGlue());
        bottomPanel.add(inputPanel);
        bottomPanel.add(Box.createHorizontalGlue());
        add(bottomPanel);

        final var zoomSlider = zoomStrip.getSlider();
        zoomSlider.addChangeListener(e -> {
            if (zoomSlider.getValueIsAdjusting()) {
                actions.setZoom(zoomSlider.getValue());
            }
        });
        add(zoomStrip);

        project.documentSettingsProperty.addChangeListener((source, settings) -> {
            if (isEnabled()) {
                refresh();
            }
        });
    }

    private JPopupMenu makeContextMenu() {
        final var res = Resources.get();
        final var contextMenu = new JPopupMenu();
        contextMenu.add(
                new ApplicationAction("previewCutAction", (event, action) -> textInput.cut())
                        .setIcon(Icons.CLIPBOARD_CUT, res.colors.icon(), res.colors.disabledIcon())
                        .setTextKey("cut")
                        .setAccelerator(KeyEvent.VK_X, Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx())
        );
        contextMenu.add(
                new ApplicationAction("previewCopyAction", (event, action) -> textInput.copy())
                        .setIcon(Icons.CLIPBOARD_COPY, res.colors.icon(), res.colors.disabledIcon())
                        .setTextKey("copy")
                        .setAccelerator(KeyEvent.VK_C, Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx())
        );
        contextMenu.add(
                new ApplicationAction("previewPasteAction", (event, action) -> textInput.paste())
                        .setIcon(Icons.CLIPBOARD_PASTE, res.colors.icon(), res.colors.disabledIcon())
                        .setTextKey("paste")
                        .setAccelerator(KeyEvent.VK_V, Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx())
        );
        return contextMenu;
    }

    @Override
    public void detach() {
        // TODO: Remove
    }

    public void refresh() {
        actions.refreshAction.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, ""));
    }

    @Override
    public void setEnabled(final boolean value) {
        super.setEnabled(value);
        textInput.setEnabled(value);
        zoomStrip.setEnabled(value);
        Actions.setEnabled(actions.all, value);
        if (value) {
            refresh();
        }
    }
}
