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
        public void set(V v) { set(v.x, v.y); }

        public void add(V v) { x += v.x; y += v.y; }

    }

    public static class VS {//vector of a size------------------------------------
        public V loc, size;
        public VS(int x, int y, int w, int h) { loc = new V(x, y); size = new V(w, h); }
        public void fill(Graphics g, Color c) { g.setColor(c); g.fillRect(loc.x, loc.y, size.x, size.y); }

        public boolean hit(int x, int y) { return x > loc.x && y > loc.y && x < (loc.x + size.x) && y < (loc.y + size.y); }

        public int xL() { return loc.x; }

        public int xH() { return loc.x + size.x; }

        public int xM() { return loc.x + size.x / 2; }

        public int yL() { return loc.y; }

        public int yH() { return loc.y + size.y; }

        public int yM() { return loc.y + size.y / 2; }
    }

    public static class LoHi {
        public int lo, hi;
        public LoHi(int min, int max) { lo = min; hi = max; }
        public void set(int val) { lo = val; hi = val; }
        public void add(int val) { if(val < lo) { lo = val; } if(val > hi) { hi = val; } }
        public int size() {
            if (hi <= lo) return 1;
            return hi - lo;
        }
    }

    //----------------------------------------------BoundingBox--------------------------------------------------------------//
    public static class BBox {
        LoHi h, v; // horizontal and vertical ranges
        public BBox() { h = new LoHi(0, 0); v = new LoHi(0, 0); }
        public void set(int x, int y) { h.set(x); v.set(y);}
        public void add(int x, int y) { h.add(x); v.add(y);}
        public void add(V v) { add(v.x, v.y); }
        public VS getNewVS() { return new VS(h.lo, v.lo, h.hi - h.lo, v.hi - v.lo); }

        public void draw(Graphics g) { g.drawRect(h.lo, v.lo, h.hi - h.lo, v.hi - v.lo); }
    }

    //-----------------------------------------------Poly-------------------------------------------------------------//
    public static class PL {
        public V[] points;
        public PL(int count) {
            points = new V[count];
            for(int i = 0; i < count; i++) { points[i] = new V(0, 0); }
        }

        public int size() { return points.length; }

        public void drawN(Graphics g, int n) {
            for(int i = 1; i < n; i++) {
                g.drawLine(points[i-1].x, points[i-1].y, points[i].x, points[i].y);
            }
        }

        public void draw(Graphics g) { drawN(g, points.length); }
    }


}
