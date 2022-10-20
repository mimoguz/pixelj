package pixelj.views.controls;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.event.MouseEvent;

import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.event.MouseInputAdapter;

import pixelj.resources.Resources;
import pixelj.util.ChangeableBoolean;
import pixelj.views.shared.Borders;
import pixelj.views.shared.Dimensions;

/**  A button which can perform another action when its right side is clicked. */
public final class CoupledActionsButton extends JButton {
    private static final int RIGHT_SIZE = 24;
    private static final int BORDER = 4;

    private final JLabel primaryActionLabel = new JLabel();
    private final JLabel secondaryActionLabel = new JLabel();
    private final ChangeableBoolean secondary = new ChangeableBoolean();
    private Action primaryAction;
    private Action secondaryAction;

    public CoupledActionsButton() {
        primaryActionLabel.setMaximumSize(Dimensions.MAXIMUM);
        primaryActionLabel.setHorizontalAlignment(SwingConstants.CENTER);
        primaryActionLabel.setBorder(Borders.EMPTY);

        secondaryActionLabel.setIconTextGap(0);
        secondaryActionLabel.setBorder(Borders.EMPTY);

        setLayout(new BorderLayout());
        setMargin(new Insets(0, 0, 0, 0));
        add(primaryActionLabel, BorderLayout.CENTER);
        add(secondaryActionLabel, BorderLayout.EAST);

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
                    action.actionPerformed(null);
                }
            }

            private void checkX(final MouseEvent e) {
                secondary.setValue(e.getX() > getWidth() - RIGHT_SIZE - BORDER);
            }
        };

        addMouseListener(mouseAdapter);
        addMouseMotionListener(mouseAdapter);
        secondary.addChangeListener(this::onSecondaryChanged);
    }

    public Action getPrimaryAction() {
        return primaryAction;
    }

    public void setPrimaryAction(final Action action) {
        primaryAction = action;
        if (action != null) {
            if (action.getValue(Action.NAME) instanceof String name) {
                primaryActionLabel.setText(name);
            }
            if (!secondary.getValue() && action.getValue(Action.SHORT_DESCRIPTION) instanceof String desc) {
                setToolTipText(desc);
            }
        }
    }

    public Action getSecondaryAction() {
        return secondaryAction;
    }

    public void setSecondaryAction(final Action action) {
        secondaryAction = action;
        setToolTipText(secondary.getValue() ? null : getToolTipText());
        setSecondaryIcon(null);
        if (action != null) {
            if (action.getValue(Action.SMALL_ICON) instanceof Icon icon) {
                setSecondaryIcon(icon);
            }
            if (secondary.getValue() && action.getValue(Action.SHORT_DESCRIPTION) instanceof String desc) {
                setToolTipText(desc);
            }
        }
    }

    public String getPrimaryText() {
        return primaryActionLabel.getText();
    }

    public void setPrimaryText(final String text) {
        primaryActionLabel.setText(text);
    }

    public Icon getSecondaryIcon() {
        return secondaryActionLabel.getIcon();
    }

    public void setSecondaryIcon(final Icon icon) {
        secondaryActionLabel.setIcon(icon);
        if (icon == null) {
            secondaryActionLabel.setBorder(Borders.EMPTY);
            return;
        }
        final var border = (RIGHT_SIZE - icon.getIconWidth()) / 2;
        secondaryActionLabel.setBorder(BorderFactory.createEmptyBorder(0, border, 0, border));
    }

    @Override
    public void setText(final String text) {
        throw new UnsupportedOperationException("Use setPrimaryText");
    }

    @Override
    public void setIcon(final Icon icon) {
        throw new UnsupportedOperationException("Use setSecondaryIcon");
    }

    @Override
    public Action getAction() {
        return primaryAction;
    }

    @Override
    public void setAction(final Action action) {
        throw new UnsupportedOperationException("Use setPrimaryAction");
    }

    @Override
    public void paint(final Graphics g) {
        super.paint(g);
        final var g2d = (Graphics2D) g.create();
        final var x = getWidth() - RIGHT_SIZE - BORDER;
        final var res = Resources.get();
        g2d.setColor(secondary.getValue() ? res.colors.accent() : res.colors.disabledIcon());
        g2d.drawLine(x, BORDER + 2, x, getHeight() - BORDER - 2);
        g2d.dispose();
    }

    private void onSecondaryChanged(final boolean value) {
        final var action = value ? secondaryAction : primaryAction;
        setToolTipText(action != null && action.getValue(Action.SHORT_DESCRIPTION) instanceof String str
            ? str
            : null
        );
        repaint();
    }
}
