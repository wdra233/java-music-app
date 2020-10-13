package reaction;

import graphicsLib.Window;
import music.UC;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public class ShapeTrainerApp extends Window {
    public static Shape.Trainer TRAINER = new Shape.Trainer();

    public ShapeTrainerApp() {
        super("ShapeTrainer", UC.WINDOW_WIDTH, UC.WINDOW_HEIGHT);
    }

    public void paintComponent(Graphics g) { TRAINER.show(g); }


    public void keyTyped(KeyEvent ke) { TRAINER.keyTyped(ke.getKeyChar()); repaint(); }

    @Override
    public void mousePressed(MouseEvent me) { TRAINER.dn(me.getX(), me.getY()); repaint(); }

    @Override
    public void mouseDragged(MouseEvent me) { TRAINER.drag(me.getX(), me.getY()); repaint(); }

    @Override
    public void mouseReleased(MouseEvent me) { TRAINER.up(me.getX(), me.getY()); repaint(); }
}
