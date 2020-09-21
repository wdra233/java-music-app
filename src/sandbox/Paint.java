package sandbox;

import graphicsLib.G;
import graphicsLib.Window;

import java.awt.*;
import java.awt.event.MouseEvent;

public class Paint extends Window {
    public static int clicks = 0;

    public Paint() {
        super("Paint", 1000, 600);
    }

    @Override
    public void paintComponent(Graphics g) {
        G.fillBack(g);
        g.setColor(G.rndColor());
        g.fillOval(100, 100, 200, 300);

        g.setColor(Color.BLACK);
        g.drawLine(100, 500, 500, 100);

        int x = 400, y = 200;
        String str = "This is totally sweet" + clicks;
        g.drawString(str, x, y);

        g.setColor(Color.RED);
        g.drawOval(x, y, 2, 2);

        FontMetrics fm = g.getFontMetrics();
        int a = fm.getAscent(), d = fm.getDescent();
        int w = fm.stringWidth(str);
        g.drawRect(x, y-a, w, a+d);


    }

    @Override
    public void mousePressed(MouseEvent me) {
        clicks++;
        repaint();
    }
}
