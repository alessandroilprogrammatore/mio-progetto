package gui.util;

import javax.swing.*;
import javax.swing.border.AbstractBorder;
import java.awt.*;

/**
 * Utility per gli elementi grafici comuni dello stile.
 */
public final class StyleUtil {
    /** Colore principale dei bottoni. */
    public static final Color PRIMARY_COLOR = new Color(243, 156, 18);

    private StyleUtil() {}

    /**
     * Crea un JButton con stile uniforme.
     *
     * @param text etichetta del bottone
     * @param size dimensione preferita, oppure null per quella di default
     * @return JButton stilizzato
     */
    public static JButton createButton(String text, Dimension size) {
        JButton btn = new JButton(text);
        if (size != null) {
            btn.setPreferredSize(size);
        }
        btn.setFont(new Font("SansSerif", Font.PLAIN, 16));
        btn.setBackground(PRIMARY_COLOR);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorder(new RoundedBorder(10));
        btn.setOpaque(true);
        btn.setContentAreaFilled(true);
        return btn;
    }

    /**
     * Bordo arrotondato per i bottoni.
     */
    public static class RoundedBorder extends AbstractBorder {
        private final int radius;
        public RoundedBorder(int radius) { this.radius = radius; }
        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(Color.WHITE);
            g2.setStroke(new BasicStroke(2));
            g2.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
        }
    }
}
