package sandbox;

import graphicsLib.G;
import graphicsLib.Window;
import music.Beam;
import music.Glyph;
import music.Page;
import music.UC;
import reaction.Gesture;
import reaction.Ink;
import reaction.Layer;
import reaction.Reaction;

import java.awt.*;
import java.awt.event.MouseEvent;

public class MusicOne extends Window {

    static {
        new Layer("BACK");
        new Layer("NOTE");
        new Layer("FORE");
    }

    public MusicOne() {
        super("MusicOne", UC.WINDOW_WIDTH, UC.WINDOW_HEIGHT);
        Reaction.initialReaction.addReaction(new Reaction("E-W") {

            public int bid(Gesture g) { return 0; }

            public void act(Gesture g) { new Page(g.vs.yM()); }
        });
    }

    static int[] xPoly = {100, 200, 200, 100};
    static int[] yPoly = {50, 70, 80, 60};
    static Polygon poly = new Polygon(xPoly, yPoly, 4);

    @Override
    protected void paintComponent(Graphics g) {
        G.fillBackGround(g);
        Layer.ALL.show(g);
        g.setColor(Color.BLACK);
        Ink.BUFFER.show(g);

//        g.setColor(Color.ORANGE);
//        int H = 8, x1 = 100, x2 = 200, y1 = 200;
//        Beam.setMasterBeam(x1, y1 + G.rnd(100), x2, y1 + G.rnd(100));
//        Beam.drawBeamStack(g, 0, 2, x1, x2, H);
//        g.setColor(Color.BLUE);
//        Beam.drawBeamStack(g, 2, 4, x1 + 10, x2 - 10, H);
//        g.fillPolygon(Beam.poly);

//        if (Page.PAGE != null) {
////            Glyph.CLEF_G.showAt(g, 8, Page.PAGE.xMargin.lo, Page.PAGE.yMargin.lo + 4 * 8);
//            int h = 32;
//            Glyph.HEAD_HALF.showAt(g,  h, 200,  Page.PAGE.yMargin.lo + 4 * h);
//            g.setColor(Color.RED);
//            g.drawRect(200, Page.PAGE.yMargin.lo + 3 * h, 25 * h / 10, 2 * h);
//        }
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
