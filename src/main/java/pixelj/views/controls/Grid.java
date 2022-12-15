package pixelj.views.controls;

import javax.swing.*;
import java.awt.*;

public class Grid<T> extends JPanel {

    private final Dimension cellSize = new Dimension(64, 64);
    private int columnCount = 4;
    private GridCellRenderer<T> cellRenderer = null;
    private ListModel<T> model = null;
    private final CellRendererPane renderPane = new CellRendererPane();

    public Grid() {
        add(renderPane);
    }

    public GridCellRenderer<T> getCellRenderer() {
        return cellRenderer;
    }

    public void setCellRenderer(GridCellRenderer<T> cellRenderer) {
        this.cellRenderer = cellRenderer;
        repaint();
    }

    public Dimension getCellSize() {
        return new Dimension(cellSize);
    }

    public void setCellSize(final int width, final int height) {
        assert width > 0;
        assert height > 0;
        cellSize.setSize(width, height);
        fixSize();
        repaint();
    }

    public int getColumnCount() {
        return columnCount;
    }

    public void setColumnCount(int columnCount) {
        assert columnCount > 0;
        this.columnCount = columnCount;
        fixSize();
        repaint();
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
        repaint();
    }

    @Override
    public Dimension getMinimumSize() {
        if (model == null) {
            return new Dimension(0, 0);
        }
        final var rows = Math.ceilDiv(model.getSize(), columnCount);
        final var cols = Math.min(model.getSize(), columnCount);
        // Leave 1 pixel gap between cells:
        return new Dimension(cols * cellSize.width + (cols - 1), rows * cellSize.height + (rows - 1));
    }

    @Override
    public void setMinimumSize(Dimension minimumSize) {
        throw new UnsupportedOperationException("Can't set the minimum size of a grid view.");
    }


    @Override
    public JComponent getComponent(final int n) {
        if (model == null || cellRenderer == null || n >= model.getSize()) {
            return null;
        }
        return cellRenderer.getComponent(model.getElementAt(n), n);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        if (model == null || cellRenderer == null) {
            return;
        }

        final var clip = g.getClipBounds();

        final var color = g.getColor();
        g.setColor(getBackground().darker());
        g.fillRect(clip.x, clip.y, clip.width, clip.height);
        g.setColor(color);

        // https://github.com/openjdk/jdk/blob/857b0f9b05bc711f3282a0da85fcff131fffab91/src/java.desktop/share/classes/javax/swing/JList.java
        // https://github.com/openjdk/jdk/blob/857b0f9b05bc711f3282a0da85fcff131fffab91/src/java.desktop/share/classes/javax/swing/plaf/basic/BasicListUI.java
        final var firstColumn = getFirstColumn(clip.x);
        final var firstRow = getFirstRow(clip.y);
        final var itemCount = model.getSize();
        final var columnCount = Math.min(this.columnCount, itemCount);
        final var rowCount = Math.ceilDiv(itemCount, this.columnCount);
        final var right = clip.x + clip.width;
        final var bottom = clip.y + clip.height;

        for (var column = firstColumn; column < columnCount; column++) {
            final var x = column * (cellSize.width + 1);
            if (x > right) {
                break;
            }
            for (var row = firstRow; row < rowCount; row++) {
                final var y = row * (cellSize.height + 1);
                if (y > bottom) {
                    break;
                }
                g.setClip(x, y, cellSize.width, cellSize.height);
                g.clipRect(x, y, cellSize.width, cellSize.height);
                final var component = getComponentAtIndex(column + row * this.columnCount);
                renderPane.paintComponent(g, component, this, x, y, cellSize.width, cellSize.height, true);
            }
        }

        renderPane.removeAll();
        g.setClip(clip);
    }

    private JComponent getComponentAtIndex(final int n) {
        if (model == null || cellRenderer == null || n >= model.getSize()) {
            return null;
        }
        return cellRenderer.getComponent(model.getElementAt(n), n);
    }

    private void fixSize() {
        final var minSize = getMinimumSize();
        super.setMinimumSize(minSize);
        renderPane.setMinimumSize(minSize);
        renderPane.setPreferredSize(minSize);
    }

    private int getFirstRow(final int y) {
        if (model == null ||model.getSize() == 0) {
            return 0;
        }
        return Math.floorDiv(y, cellSize.height + 1);
    }

    private int getFirstColumn(final int x) {
        if (model == null ||model.getSize() == 0) {
            return 0;
        }
        return Math.floorDiv(x, cellSize.width + 1);
    }

    public interface GridCellRenderer<T> {
        JComponent getComponent(final T value, final int index);

        JComponent getEmpty(final Dimension size);
    }
}
