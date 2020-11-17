package music;

import graphicsLib.G;
import reaction.Gesture;
import reaction.Reaction;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Stem extends Duration implements Comparable<Stem> {
    public Staff staff;
    public Head.List heads = new Head.List();
    boolean isUp = true;
    public Beam beam = null;

    public Stem(Staff staff, boolean up) {
        super();
        this.staff = staff;
        isUp = up;
//        staff.sys.stems.add(this); // This is done in stem head in Time class
        addReaction(new Reaction("E-E") { // increment the flags
            @Override
            public int bid(Gesture g) {
                return bidLineCrossStem(g.vs.yM(), g.vs.xL(), g.vs.xH(), Stem.this);
            }

            @Override
            public void act(Gesture g) {
                Stem.this.incFlag();
            }
        });

        addReaction(new Reaction("W-W") { // decrement the flags
            @Override
            public int bid(Gesture g) {
                return bidLineCrossStem(g.vs.yM(), g.vs.xL(), g.vs.xH(), Stem.this);
            }

            @Override
            public void act(Gesture g) {
                Stem.this.decFlag();
            }
        });
    }

    public static int bidLineCrossStem(int y, int x1, int x2, Stem stem) {
        if (stem.heads.size() > 0) {
            int xs = stem.heads.get(0).time.x;
            if (x1 > xs || x2 < xs) {
                return UC.NO_BID;
            }
            int y1 = stem.yLo(), y2 = stem.yHi();
            if (y < y1 || y > y2) {
                return UC.NO_BID;
            }
            return Math.abs(y - (y1 + y2) / 2);
        }
        return UC.NO_BID;
    }

    @Override
    public void show(Graphics g) {
        if (nFlag >= -1 && heads.size() > 0) {
            int x = x(), yH = yFirstHead(), yB = yBeamEnd(), h = staff.H();
            g.drawLine(x, yH, x, yB);
            if (nFlag > 0) {
                if (nFlag == 1) { (isUp? Glyph.FLAG1D : Glyph.FLAG1U).showAt(g, h, x, yB); }
                if (nFlag == 2) { (isUp? Glyph.FLAG2D : Glyph.FLAG2U).showAt(g, h, x, yB); }
                if (nFlag == 3) { (isUp? Glyph.FLAG3D : Glyph.FLAG3U).showAt(g, h, x, yB); }
                if (nFlag == 4) { (isUp? Glyph.FLAG4D : Glyph.FLAG4U).showAt(g, h, x, yB); }
            }
        }
    }

    public Head firstHead() { return heads.get(isUp ? heads.size() - 1 : 0); }
    public Head lastHead() { return heads.get(isUp ? 0 : heads.size() - 1); }

    public int yFirstHead() { Head h = firstHead(); return h.staff.yLine(h.line); }
    public int x() { Head h = firstHead(); return h.time.x +( isUp ? h.w() : 0 ); }
    public int yBeamEnd() {
        Head h = lastHead();
        int line = h.line;
        line += (isUp ? -7 : 7); // default extension y octave;
        int flagIncrement = nFlag > 2 ? 2 * (nFlag - 2) : 0; // if more than 2 flags, just the end
        line += isUp ? -flagIncrement : flagIncrement;
        if ((isUp && line > 4) || (!isUp && line < 4)) { line = 4; }
        return h.staff.yLine(line);
    }

    public int yLo() {
        return isUp ? yBeamEnd() : yFirstHead();
    }

    public int yHi() {
        return isUp ? yFirstHead() : yBeamEnd();
    }

    public void deleteStem() {
        deleteMass();
    }

    public void setWrongSize() {
        Collections.sort(heads);
        int i, last, next;
        if(isUp) {
            i = heads.size() - 1;
            last = 0;
            next = -1;
        } else {
            i = 0;
            last = heads.size() - 1;
            next = 1;
        }
        Head pH = heads.get(i);
        pH.wrongSide = false;
        while (i != last) {
            i += next;
            Head nH = heads.get(i);
            nH.wrongSide = ( pH.staff == nH.staff && Math.abs(nH.line - pH.line) <= 1) && !pH.wrongSide;
            pH = nH;
        }
    }

    @Override
    public int compareTo(Stem s) {
        return x() - s.x();
    }

    // ----------------------------List Class------------------------------------
    public static class List extends ArrayList<Stem> {
        public G.LoHi yRange;
        public void addStem(Stem s) {
            if (yRange == null) { yRange = new G.LoHi(s.yLo(), s.yHi()); }
            yRange.add(s.yLo());
            yRange.add(s.yHi());
            add(s);
        }

        public boolean fastReject(int y1, int y2) {
            return y1 > yRange.hi || y2 < yRange.lo;
        }

        public void sort() {
            Collections.sort(this);
        }
    }
}
