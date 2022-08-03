package pixelj.views.controls;

import java.awt.event.MouseEvent;
import java.awt.Insets;
import java.awt.Color;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.event.MouseInputAdapter;

import net.miginfocom.swing.MigLayout;
import pixelj.util.ChangeableBoolean;

public final class CoupledActionsButton extends JButton {
    private final JLabel primaryActionLabel = new JLabel();
    private final JLabel secondaryActionLabel = new JLabel();
    private final ChangeableBoolean secondary = new ChangeableBoolean();
    private Action primaryAction;
    private Action secondaryAction;

    public CoupledActionsButton() {
        setLayout(new MigLayout("", "[grow]8lp[24lp!]", "[center]"));
        add(primaryActionLabel);
        add(secondaryActionLabel);

        setMargin(new Insets(0, 0, 0, 0));

        final var mouseAdapter = new MouseInputAdapter() {
            @Override
            public void mouseEntered(final MouseEvent e) {
                checkX(e);
            }

            @Override
            public void mouseExited(final MouseEvent e) {
                secondary.setValue(false);
            }

            @Override
            public void mouseMoved(final MouseEvent e) {
                checkX(e);
            }

            @Override
            public void mouseClicked(final MouseEvent e) {
                final var action = secondary.getValue() ? getSecondaryAction() : getAction();
                if (action != null) {
                    action.accept(CoupledActionsButton.this);
                }
            }

            private void checkX(final MouseEvent e) {
                secondary.setValue(e.getX() > getWidth() - 24);
            }
        };

        addMouseListener(mouseAdapter);
        addMouseMotionListener(mouseAdapter);
    }

    public Action getSecondaryAction() {
        return secondaryAction;
    }

    public void setSecondaryAction(final Action action) {
        secondaryAction = action;
        if (action != null && action.getValue(Action.SMALL_ICON) instanceof Icon icon) {
            secondaryActionLabel.setIcon(icon);
        }
    }

    @Override
    public String getText() {
        if (primaryActionLabel != null) {
            return primaryActionLabel.getText();
        }
        return null;
    }

    @Override
    public void setText(final String text) {
        primaryActionLabel.setText(text);
    }

    @Override
    public Icon getIcon() {
        return secondaryActionLabel.getIcon();
    }

    @Override
    public void setIcon(final Icon icon) {
        secondaryActionLabel.setIcon(icon);
    }

    @Override
    public Action getAction() {
        return primaryAction;
    }

    @Override
    public void setAction(final Action action) {
        primaryAction = action;
        if (action != null && action.getValue(Action.NAME) instanceof String name) {
            primaryActionLabel.setText(name);
        }
    }
}
