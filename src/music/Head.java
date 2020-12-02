package music;

import graphicsLib.G;
import reaction.Gesture;
import reaction.Layer;
import reaction.Mass;
import reaction.Reaction;

import java.awt.*;
import java.util.ArrayList;

public class Head extends Mass implements Comparable<Head> {

    public Staff staff;
    public int line;
    public Time time;
    public Glyph forcedGlyph = null;
    public Stem stem = null;
    public boolean wrongSide = false;

    public Head(Staff staff, int x, int y) {
        super("NOTE"); // note layer
        this.staff = staff;
        this.time = staff.sys.getTime(x);
        time.heads.add(this);
        line = staff.lineOfY(y);

        addReaction(new Reaction("S-S") { // stem or unstem heads
            @Override
            public int bid(Gesture g) {
                int x = g.vs.xM(), y1 = g.vs.yL(), y2 = g.vs.yH();
                int w = Head.this.w(), yH = Head.this.y();
                if (y1 > yH || y2 < yH) return UC.NO_BID;
                int hL = Head.this.time.x, hR = hL + w;
                if ( x < hL - w || x > hR + w ) return UC.NO_BID;
                if (x < hL + w / 2) return hL - x;
                if (x > hR - w / 2) return x - hR;
                return UC.NO_BID;
            }

            @Override
            public void act(Gesture g) {
                int x = g.vs.xM(), y1 = g.vs.yL(), y2 = g.vs.yH();
                Staff staff = Head.this.staff;
                Time t = Head.this.time;
                int w = Head.this.w();
                boolean up = x >t.x + w / 2;
                if (Head.this.stem == null) {
//                    t.stemHead(staff, up, y1, y2);
                    Stem.getStem(staff, time, y1, y2, up);
                } else {
                    t.unStemHead(y1, y2);
                }
            }
        });

        addReaction(new Reaction("DOT") {
            @Override
            public int bid(Gesture g) {
                int xH = Head.this.x(), yH = Head.this.y(), h = Head.this.staff.H(), w = Head.this.w();
                int x = g.vs.xM(), y = g.vs.yM();
                if (x < xH || x > xH + 2 * w || y < yH - h || y > yH + h) return UC.NO_BID;
                return Math.abs(xH + w - x) + Math.abs(yH - y);
            }

            @Override
            public void act(Gesture g) {
                if (Head.this.stem != null) {
                    Head.this.stem.cycleDot();
                }
            }
        });

        addReaction(new Reaction("S-N") { // To delete a head
            @Override
            public int bid(Gesture g) {
                int w2 = Head.this.w() / 2, h = Head.this.staff.H();
                int x = g.vs.xM(), xH = x() + w2, dx = Math.abs(x - xH);
                if (dx > 3 * w2) { return UC.NO_BID; }
                int y = g.vs.yL(), yH = y(), dy = Math.abs(y - yH);
                if (dy > 3 * h) { return UC.NO_BID; }
                return dx + dy;

            }

            @Override
            public void act(Gesture g) {
                Head.this.deleteHead();
            }
        });
    }

    public void show(Graphics g) {
        int h = staff.H();
        g.setColor(stem == null ? Color.GRAY : Color.BLACK);
        (forcedGlyph != null ? forcedGlyph : normalGlyph()).showAt(g, h, x(), staff.yTop() + line * h);
        if (stem != null) {
            int off = UC.REST_AUG_DOT_OFF_SET, sp = UC.AUG_DOT_SPACING;
            for(int i = 0; i < stem.nDot; i++) {
                g.fillOval(time.x + off + i * sp, y() - 3 * h / 2, 2 * h / 3, 2 * h / 3);
            }
        }
    }

    public void deleteHead() {
        unStem();
        time.removeHead(this);
        deleteMass();
    }

    public void unStem() {
        if (stem != null) {
            stem.heads.remove(this);
            if (stem.heads.size() ==0) {
                stem.deleteStem();
            } else {
                System.out.println("here----------------------");
                stem.setWrongSize();
            }
            stem = null;
            wrongSide = false;
        }
    }

    public void joinStem(Stem s) {
        if (stem != null) { unStem(); }
        s.heads.add(this);
        stem = s;
    }

    public Glyph normalGlyph() {
        if (stem == null) { return Glyph.HEAD_Q; }
        if (stem.nFlag == -1) { return Glyph.HEAD_HALF; }
        if (stem.nFlag == -2) { return Glyph.HEAD_WHOLE; }
        return Glyph.HEAD_Q;
    }

    public int y() { return staff.yLine(line); }
    public int x() {
        int res = time.x;
        if (wrongSide) {
            res += (stem != null && stem.isUp) ? w() : -w();
        }
        return res;
    }
    public int w() {
        return 24 * staff.H() / 10;
    }

    @Override
    public int compareTo(Head head) {
        return ( staff.iStaff != head.staff.iStaff ) ? staff.iStaff - head.staff.iStaff : line - head.line;
    }

    public static class List extends ArrayList<Head> {

    }

}
