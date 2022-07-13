package pixelj.views.shared;

import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.WindowConstants;
import javax.swing.border.Border;

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
}
