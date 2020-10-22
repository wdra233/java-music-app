package sandbox;

import graphicsLib.G;
import graphicsLib.Window;
import music.Page;
import music.UC;
import reaction.Gesture;
import reaction.Ink;
import reaction.Layer;
import reaction.Reaction;

import java.awt.*;
import java.awt.event.MouseEvent;

public class MusicOne extends Window {

    public Layer BACK = new Layer("BACK"), FORE = new Layer("FORE");

    public MusicOne() {
        super("MusicOne", UC.WINDOW_WIDTH, UC.WINDOW_HEIGHT);
        Reaction.initialReaction.addReaction(new Reaction("E-W") {

            public int bid(Gesture g) { return 0; }

            public void act(Gesture g) { new Page(g.vs.yM()); }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        G.fillBackGround(g);
        Layer.ALL.show(g);
        g.setColor(Color.BLACK);
        Ink.BUFFER.show(g);
    }

    @Override
    public void mousePressed(MouseEvent me) { Gesture.AREA.dn(me.getX(), me.getY()); repaint(); }

    @Override
    public void mouseDragged(MouseEvent me) { Gesture.AREA.drag(me.getX(), me.getY()); repaint(); }

    @Override
    public void mouseReleased(MouseEvent me) {
        Gesture.AREA.up(me.getX(), me.getY()); repaint();
    }
}
