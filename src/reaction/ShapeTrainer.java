package reaction;

import graphicsLib.G;
import graphicsLib.Window;
import music.UC;

import java.awt.*;
import java.awt.event.KeyEvent;

public class ShapeTrainer extends Window {
    public static String UNKNOWN = "<-This name is currently unknown";
    public static String ILLEGAL = "<-illegal>";
    public static String KNOWN = "<-known";
    public static String curName = "";
    public static String curState = ILLEGAL;

    public ShapeTrainer() {
        super("ShapeTrainer", UC.WINDOW_WIDTH, UC.WINDOW_HEIGHT);
    }

    public void paintComponent(Graphics g) {
        G.fillBackGround(g);
        g.setColor(Color.BLACK);
        g.drawString(curName, 600, 30);
        g.drawString(curState, 700, 30);
    }

    public void setState() {
        curState = (curName.equals("") || curName.equals("DOT")) ? ILLEGAL : UNKNOWN;
    }

    public void keyTyped(KeyEvent ke) {
        char c = ke.getKeyChar();
        System.out.println("typed: " + c);
        curName = (c == ' ' || c == 0x0D || c == 0x0A) ? "" : curName + c;
        setState();
        repaint();
    }
}
