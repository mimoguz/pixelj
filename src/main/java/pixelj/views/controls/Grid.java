package pixelj.views.controls;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class Grid<T> extends JPanel {

    private final Dimension cellSize = new Dimension(64, 64);
    private int columnCount = 4;
    private GridCellRenderer<T> cellRenderer = null;
    private ListModel<T> model = null;
    private final CellRendererPane renderPane = new CellRendererPane();

    private Color lineColor;

    public Grid() {
        super();
        add(renderPane);
        lineColor = getBackground().darker();
    }

    public GridCellRenderer<T> getCellRenderer() {
        return cellRenderer;
    }

    public void setCellRenderer(GridCellRenderer<T> cellRenderer) {
        this.cellRenderer = cellRenderer;
        if (isShowing()) {
            repaint();
        }
    }

    public Dimension getCellSize() {
        return new Dimension(cellSize);
    }

    public void setCellSize(final int width, final int height) {
        assert width > 0;
        assert height > 0;
        cellSize.setSize(width, height);
        fixSize();
        if (isShowing()) {
            repaint();
        }
    }

    public int getColumnCount() {
        return columnCount;
    }

    public void setColumnCount(int columnCount) {
        assert columnCount > 0;
        this.columnCount = columnCount;
        fixSize();
        if (isShowing()) {
            repaint();
        }
    }

    public Color getLineColor() {
        return lineColor;
    }

    public void setLineColor(final Color lineColor) {
        this.lineColor = lineColor;
        if (isShowing()) {
            repaint();
        }
    }

    public ListModel<T> getModel() {
        return model;
    }

    public void setModel(ListModel<T> model) {
        if (this.model != null) {
            // Remove listener
        }
        this.model = model;
        if (model != null) {
            // Add listener
        }
        fixSize();
        if (isShowing()) {
            repaint();
        }
    }

    @Override
    public void setBorder(final Border border) {
        super.setBorder(border);
        fixSize();
        if (isShowing()) {
            repaint();
        }
    }

    @Override
    public Dimension getMinimumSize() {
        final var sz = cellSize == null ? new Dimension(0, 0) : cellSize;
        if (model == null || model.getSize() == 0) {
            return new Dimension(sz.width, sz.height);
        }
        final var rows = Math.ceilDiv(model.getSize(), columnCount);
        final var cols = Math.min(model.getSize(), columnCount);
        final var insets = getInsets();
        // Leave 1 pixel gap between cells:
        return new Dimension(
            cols * sz.width + (cols - 1) + insets.left + insets.right,
            rows * sz.height + (rows - 1) + insets.top + insets.bottom
        );
    }

    @Override
    public void setMinimumSize(Dimension minimumSize) {
        throw new UnsupportedOperationException("Can't set the minimum size of a grid view.");
    }

    // TODO: Don't paint the invisible parts.
    @Override
    public void paintChildren(Graphics g) {
        // super.paint(g);
        if (model == null || cellRenderer == null) {
            return;
        }

        final var clip = g.getClipBounds();
        final var color = g.getColor();
        g.setColor(lineColor);
        g.fillRect(clip.x, clip.y, clip.width , clip.height);
        g.setColor(color);

        // https://github.com/openjdk/jdk/blob/857b0f9b05bc711f3282a0da85fcff131fffab91/src/java.desktop/share/classes/javax/swing/JList.java
        // https://github.com/openjdk/jdk/blob/857b0f9b05bc711f3282a0da85fcff131fffab91/src/java.desktop/share/classes/javax/swing/plaf/basic/BasicListUI.java
        final var numItems = model.getSize();
        final var numColumns = Math.min(columnCount, numItems);
        final var numRows = Math.ceilDiv(numItems, columnCount);

        if (numItems == 0) {
            final var component = cellRenderer.getEmpty("Empty");
            renderPane.paintComponent(g, component, this, 0, 0, cellSize.width, cellSize.height, true);
        } else {
            for (var column = 0; column < numColumns; column++) {
                final var x = column * (cellSize.width + 1);
                for (var row = 0; row < numRows; row++) {
                    final var y = row * (cellSize.height + 1);
                    final var component = getComponentAtIndex(column + row * this.columnCount);
                    renderPane.paintComponent(g, component, this, x, y, cellSize.width, cellSize.height, true);
                }
            }
        }

        renderPane.removeAll();
        g.setClip(clip);
    }

    private JComponent getComponentAtIndex(final int n) {
        if (model == null) {
            return  null;
        }
        if (cellRenderer == null) {
            return new JPanel();
        }
        if (n >= model.getSize()) {
            return cellRenderer.getEmpty(null);
        }
        return cellRenderer.getComponent(model.getElementAt(n), n);
    }

    private void fixSize() {
        final var minSize = getMinimumSize();
        super.setMinimumSize(minSize);
        if (renderPane == null) {
            return;
        }
        renderPane.setMinimumSize(minSize);
        renderPane.setPreferredSize(minSize);
    }

    private int getFirstRow(final int y) {
        if (model == null || model.getSize() == 0) {
            return 0;
        }
        return Math.floorDiv(y, cellSize.height + 1);
    }

    private int getFirstColumn(final int x) {
        if (model == null || model.getSize() == 0) {
            return 0;
        }
        return Math.floorDiv(x, cellSize.width + 1);
    }

    public interface GridCellRenderer<T> {
        JComponent getComponent(final T value, final int index);

        JComponent getEmpty(final String message);
    }
}
