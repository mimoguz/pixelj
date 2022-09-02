package pixelj.views.shared;

import java.awt.Frame;
import java.awt.event.KeyEvent;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComponent;
import javax.swing.KeyStroke;

import pixelj.services.AppState;

public final class OptionsDialog extends OptionsDialogBase {

    private AppState.Theme result;

    public OptionsDialog(final Frame owner) {
        super(owner);

        themeIn.setModel(new DefaultComboBoxModel<>(AppState.Theme.values()));
        themeIn.setSelectedIndex(0);

        saveButton.addActionListener(e -> {
            result = AppState.Theme.values()[themeIn.getSelectedIndex()];
            setVisible(false);
        });

        cancelButton.addActionListener(e -> setVisible(false));

        getRootPane().registerKeyboardAction(
            (e) -> setVisible(false),
            KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
            JComponent.WHEN_IN_FOCUSED_WINDOW
        );
    }

    public void setTheme(final AppState.Theme theme) {
        themeIn.setSelectedIndex(theme.ordinal());
    }

    public AppState.Theme getResult() {
        return result;
    }

    @Override
    public void setVisible(final boolean visible) {
        if (visible) {
            result = null;
        }
        super.setVisible(visible);
    }
}
