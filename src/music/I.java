package music;

import reaction.Gesture;

import java.awt.*;

public interface I {
    public interface Hit { public boolean hit(int x, int y); }
    public interface Area extends Hit {
        public void dn(int x, int y);
        public void drag(int x, int y);
        public void up(int x, int y);
    }
    interface Show {
        public void show(Graphics g);
    }
    interface Act { public void act(Gesture g); }
    interface React extends Act { public int bid(Gesture g); }
}
