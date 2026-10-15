package mvc.view.impl;

import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JComponent;
import javax.swing.JPanel;

/**
 * Base for the menu screens: the game background with components stacked in the center.
 */
public class BackgroundPanel extends JPanel {

    private static final long serialVersionUID = 1L;
    private static final int GAP = 8;

    private int row;

    /**
     * Creates an empty screen with the game background.
     */
    public BackgroundPanel() {
        super(new GridBagLayout());
    }

    /**
     * Adds a component under the previous one, centered.
     *
     * @param component to add
     */
    protected final void addRow(final JComponent component) {
        final GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = this.row;
        c.insets = new Insets(GAP, 0, GAP, 0);
        c.fill = GridBagConstraints.HORIZONTAL;
        this.add(component, c);
        this.row++;
    }

    @Override
    protected final void paintComponent(final Graphics g) {
        super.paintComponent(g);
        UiStyle.background().ifPresent(bg -> g.drawImage(bg, 0, 0, this));
    }
}
