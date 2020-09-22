package graphicsLib;

import java.awt.*;
import java.util.Random;

public class G {
    public static Random RND = new Random();

    public static int rnd(int max) {
        return RND.nextInt(max);
    }

    public static Color rndColor() {
        return new Color(rnd(256), rnd(256), rnd(256));
    }

    public static void fillBackGround(Graphics g) {
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, 5000, 5000);
    }

    public static class V {//vector----------------------------------------------
        public int x, y;

        public V(int x, int y) { set(x, y); }

        public void set(int x, int y) { this.x = x; this.y = y; }

    }

    public static class VS {//vector of a size------------------------------------
        public V loc, size;
        public VS(int x, int y, int w, int h) { loc = new V(x, y); size = new V(w, h); }
        public void fill(Graphics g, Color c) { g.setColor(c); g.fillRect(loc.x, loc.y, size.x, size.y); }

        public boolean hit(int x, int y) { return x > loc.x && y > loc.y && x < (loc.x + size.x) && y < (loc.y + size.y); }
    }

    public static class LoHi {
    }

    public static class BBox {
    }

    public static class PL {
    }


}
