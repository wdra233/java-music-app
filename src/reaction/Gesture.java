package reaction;

import graphicsLib.G;
import music.I;

public class Gesture {
    public Shape shape;
    public G.VS vs;
    public static I.Area AREA = new I.Area() {

        @Override
        public void dn(int x, int y) {
            Ink.BUFFER.dn(x, y);
        }

        @Override
        public void drag(int x, int y) {
            Ink.BUFFER.drag(x, y);
        }

        @Override
        public void up(int x, int y) {
            Ink.BUFFER.add(x, y);
            Ink ink = new Ink();
            Gesture gesture = Gesture.getNew(ink); // This can fail if not recognized.
            Ink.BUFFER.clear();
            if (gesture != null) {
                Reaction r = Reaction.best(gesture); // fail if there is no bid
                if (r != null) {
                    r.act(gesture);
                }
            }
        }

        @Override
        public boolean hit(int x, int y) {
            return true;
        }
    };



    private Gesture(Shape shape, G.VS vs) {
        this.shape = shape;
        this.vs = vs;
    }

    public static Gesture getNew(Ink ink) { // can return null
        Shape s = Shape.recognize(ink);
        return (s == null) ? null : new Gesture(s, ink.vs);
    }
}
