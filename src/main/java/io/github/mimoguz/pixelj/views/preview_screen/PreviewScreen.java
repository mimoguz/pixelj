package io.github.mimoguz.pixelj.views.preview_screen;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.*;

import com.formdev.flatlaf.FlatClientProperties;

import io.github.mimoguz.pixelj.actions.Actions;
import io.github.mimoguz.pixelj.actions.ApplicationAction;
import io.github.mimoguz.pixelj.actions.PreviewScreenActions;
import io.github.mimoguz.pixelj.controls.PromptTextArea;
import io.github.mimoguz.pixelj.models.ProjectModel;
import io.github.mimoguz.pixelj.resources.Icons;
import io.github.mimoguz.pixelj.resources.Resources;
import io.github.mimoguz.pixelj.util.Detachable;
import io.github.mimoguz.pixelj.views.shared.Borders;
import io.github.mimoguz.pixelj.views.shared.Components;
import io.github.mimoguz.pixelj.views.shared.Dimensions;

public class PreviewScreen extends JPanel implements Detachable {
    private static final long serialVersionUID = -4480174487039009081L;

    private final transient PreviewScreenActions actions;
    private final JButton clearButton;
    private final JPanel container;
    private final transient ProjectModel project;
    private final JButton refreshButton;
    private final PromptTextArea textInput;
    private final JSlider zoomSlider;

    public PreviewScreen(final ProjectModel project, final JComponent root) {
        this.project = project;

        final var res = Resources.get();

        textInput = new PromptTextArea();
        textInput.setMaximumSize(Dimensions.MAXIMUM);
        textInput.setPromptText(res.getString("previewTextInputPrompt"));

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
        textInput.setComponentPopupMenu(contextMenu);

        container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        container.setBorder(
                BorderFactory.createMatteBorder(
                        Dimensions.PADDING,
                        Dimensions.PADDING,
                        Dimensions.PADDING,
                        Dimensions.PADDING,
                        Color.WHITE
                )
        );
        container.setBackground(Color.WHITE);
        actions = new PreviewScreenActions(project, textInput, container);

        refreshButton = new JButton();
        refreshButton.setAction(actions.refreshAction);
        Components.setFixedSize(refreshButton, Dimensions.TEXT_BUTTON_SIZE);

        clearButton = new JButton();
        clearButton.setAction(actions.clearAction);
        Components.setFixedSize(clearButton, Dimensions.TEXT_BUTTON_SIZE);

        Actions.registerShortcuts(actions.all, root);

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        final var title = new JLabel(res.getString("previewTitle"));
        title.putClientProperty(FlatClientProperties.STYLE_CLASS, "h4");
        final var titlePanel = new JPanel();
        titlePanel.setBorder(Borders.titleCenter);
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.X_AXIS));
        titlePanel.add(Box.createHorizontalGlue());
        titlePanel.add(title);
        titlePanel.add(Box.createHorizontalGlue());
        add(titlePanel);

        final var previewPanel = new JPanel(new GridBagLayout());
        previewPanel.add(container);
        final var scrollPanel = new JScrollPane(previewPanel);
        scrollPanel.setMaximumSize(Dimensions.MAXIMUM);
        scrollPanel.setBorder(Borders.smallEmptyCenter);
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
        bottomPanel.setBorder(Borders.smallEmptyBottomCenterPanel);
        bottomPanel.add(Box.createHorizontalGlue());
        bottomPanel.add(inputPanel);
        bottomPanel.add(Box.createHorizontalGlue());
        add(bottomPanel);

        zoomSlider = new JSlider(1, 48, 1);
        zoomSlider.setMinimumSize(new Dimension(96, 24));
        zoomSlider.setMaximumSize(new Dimension(256, 24));
        zoomSlider.addChangeListener(e -> {
            if (zoomSlider.getValueIsAdjusting()) {
                actions.setZoom(zoomSlider.getValue());
            }
        });
        final var zoomPanel = new JPanel();
        zoomPanel.setBorder(Borders.smallEmptyCupCenter);
        zoomPanel.setLayout(new BoxLayout(zoomPanel, BoxLayout.X_AXIS));
        zoomPanel.add(Box.createHorizontalGlue());
        zoomPanel.add(zoomSlider);
        zoomPanel.add(Box.createHorizontalGlue());
        add(zoomPanel);
    }

    @Override
    public void detach() {
        // TODO: Remove
    }

    public JButton getClearButton() {
        return clearButton;
    }

    public JButton getRefreshButton() {
        return refreshButton;
    }

    public JTextArea getTextInput() {
        return textInput;
    }

    public void refresh() {
        actions.refreshAction.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, ""));
    }

    @Override
    public void setEnabled(final boolean value) {
        super.setEnabled(value);
        textInput.setEnabled(value);
        Actions.setEnabled(actions.all, value);
        if (value) {
            refresh();
        }
    }
}
