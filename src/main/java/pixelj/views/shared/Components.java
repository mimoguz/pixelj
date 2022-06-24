package pixelj.views.shared;

import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.WindowConstants;
import javax.swing.border.Border;

public class Components {
    public static void addInnerBorder(final JComponent component, final Border border) {
        component.setBorder(BorderFactory.createCompoundBorder(component.getBorder(), border));
    }

    public static void addOuterBorder(final JComponent component, final Border border) {
        component.setBorder(BorderFactory.createCompoundBorder(border, component.getBorder()));
    }

    public static void setFixedSize(final JComponent component, final Dimension size) {
        component.setMinimumSize(size);
        component.setMaximumSize(size);
        component.setPreferredSize(size);
        component.setSize(size);
        component.revalidate();
    }

    public static void switchFrames(JFrame oldFrame, JFrame newFrame) {
        oldFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        newFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        newFrame.setVisible(true);
        oldFrame.setVisible(false);
    }
}
