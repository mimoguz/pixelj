package pixelj.views.shared;

import java.awt.Frame;
import java.awt.event.KeyEvent;

import javax.swing.JComponent;
import javax.swing.KeyStroke;

public final class OptionsDialog extends OptionsDialogBase {

    private Boolean result;

    public OptionsDialog(final Frame owner) {
        super(owner);

        saveButton.addActionListener(e -> {
            result = themeIn.isSelected();
            setVisible(false);
        });

        cancelButton.addActionListener(e -> setVisible(false));

        getRootPane().registerKeyboardAction(
            (e) -> setVisible(false),
            KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
            JComponent.WHEN_IN_FOCUSED_WINDOW
        );
    }

    public void setDarkTheme(final boolean useDarkTheme) {
        themeIn.setSelected(useDarkTheme);
    }

    public Boolean getResult() {
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
