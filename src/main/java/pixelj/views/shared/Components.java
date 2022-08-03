package pixelj.views.shared;

import java.awt.Dimension;

import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.KeyStroke;
import javax.swing.WindowConstants;
import javax.swing.border.Border;

import pixelj.actions.ApplicationAction;

public final class Components {

    private Components() {
    }

    /**
     * Add the border inside of the component's existing border.
     *
     * @param component
     * @param border    The new border
     */
    public static void addInnerBorder(final JComponent component, final Border border) {
        component.setBorder(BorderFactory.createCompoundBorder(component.getBorder(), border));
    }

    /**
     * Add the border outside of the component's existing border.
     *
     * @param component
     * @param border    The new border
     */
    public static void addOuterBorder(final JComponent component, final Border border) {
        component.setBorder(BorderFactory.createCompoundBorder(border, component.getBorder()));
    }

    /**
     * Set component's dimensions fixed.
     *
     * @param component
     * @param size
     */
    public static void setFixedSize(final JComponent component, final Dimension size) {
        component.setMinimumSize(size);
        component.setMaximumSize(size);
        component.setPreferredSize(size);
        component.setSize(size);
        component.revalidate();
    }

    /**
     * Switch between frames. This will close the old frame.
     *
     * @param oldFrame From
     * @param newFrame To
     */
    public static void switchFrames(final JFrame oldFrame, final JFrame newFrame) {
        oldFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        newFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        newFrame.setVisible(true);
        oldFrame.setVisible(false);
    }

    /**
     * If action has an accelerator defined, register it to the component as a shortcut key.
     *
     * @param component
     * @param action
     */
    public static void registerShortcut(final JComponent component, final ApplicationAction action) {
        if (action.getValue(Action.ACCELERATOR_KEY) instanceof KeyStroke accelerator) {
            component.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(accelerator, action.getKey());
            component.getActionMap().put(action.getKey(), action);
        }
    }

    /**
     * If action has an accelerator defined, unregister it from the component.
     *
     * @param component
     * @param action
     */
    public static void unregisterShortcut(final JComponent component, final ApplicationAction action) {
        if (action.getValue(Action.ACCELERATOR_KEY) instanceof KeyStroke accelerator) {
            component.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).remove(accelerator);
            component.getActionMap().remove(action.getKey());
        }
    }

}
