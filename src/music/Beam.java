package music;

import reaction.Mass;

import java.awt.*;

public class Beam extends Mass {
    public Stem.List stems = new Stem.List();

    public Beam(Stem firstStem, Stem lastStem) {
        super("NOTE");
        stems.add(firstStem);
        stems.add(lastStem);
    }

    public Stem first() {
        return stems.get(0);
    }

    public Stem last() {
        return stems.get(stems.size() - 1);
    }

    public void deleteBeam() {
        for (Stem s : stems) {
            s.beam = null;
        }
        deleteMass();
    }

    public void addStem(Stem s) {
        if (s.beam == null) {
            stems.add(s);
            s.beam = this;
            s.nFlag = 1;
            stems.sort();
        }
    }

    public static int yOfX(int x, int x1, int y1, int x2, int y2) {
        int dY = y2 - y1, dX = x2 - x1;
        return (x - x1) * dY / dX + y1;
    }

    public static int yOfX(int x) {
        return yOfX(x, mX1, mY1, mX2, mY2);
    }

    public static int mX1, mX2, mY1, mY2; // These are coordinates for master beam

    public static void setMasterBeam(int x1, int y1, int x2, int y2) {
        mX1 = x1;
        mX2 = x2;
        mY1 = y1;
        mY2 = y2;
    }

    public void setMasterBeam() {
        mX1 = first().x();
        mY1 = first().yBeamEnd();
        mX2 = last().x();
        mY2 = last().yBeamEnd();
    }

    public static Polygon poly;

    static {
        int[] foo = {0, 0, 0, 0};
        poly = new Polygon(foo, foo, 4);
    }

    public static void setPoly(int x1, int y1, int x2, int y2, int height) {
        int[] a = poly.xpoints; a[0] = x1; a[1] = x2; a[2] = x2; a[3] = x1;
        a = poly.ypoints; a[0] = y1; a[1] = y2; a[2] = y2 + height; a[3] = y1 + height;
    }

    public static void drawBeamStack(Graphics g, int n1, int n2, int x1, int x2, int h) {
        int y1 = yOfX(x1), y2 = yOfX(x2);
        for (int i = n1; i < n2; i++) {
            setPoly(x1, y1 + i * 2 * h, x2, y2 + i * 2 * h, h);
            g.fillPolygon(poly);
        }

    }

    public void drawBeamGroup(Graphics g) {
        setMasterBeam();
        Stem firstStem = first();
        int H = firstStem.staff.H(), sH = firstStem.isUp ? H : -H;
        int nPrev = 0, nCur = firstStem.nFlag, nNext = stems.get(1).nFlag;
        int pX = 0, cX = firstStem.x(), bX = cX + 3 * H;
        if (nCur > nNext) { drawBeamStack(g, nNext, nCur, cX, bX, sH);}
        for(int cur = 1; cur < stems.size(); cur++) {
            Stem sCur = stems.get(cur);
            pX = cX;
            cX = sCur.x();
            nPrev = nCur;
            nCur = nNext;
            nNext = (cur < stems.size() - 1 ) ? stems.get(cur + 1).nFlag : 0;
            int nBack = Math.min(nPrev, nCur);
            drawBeamStack(g, 0, nBack, pX, cX, sH);
            if (nCur > nPrev && nCur > nNext) { // We need beamlet on the current stem
                if (nPrev < nNext) {
                    bX = cX +  3 * H;
                    drawBeamStack(g, nNext, nCur, cX, bX, sH);
                } else {
                    bX = cX - 3 * H;
                    drawBeamStack(g, nPrev, nCur, bX, cX, sH);
                }
            }
        }
    }

    public void show(Graphics g) { g.setColor(Color.BLACK); drawBeamGroup(g);}

    public static boolean verticalLineCrossesSegment(int x, int y1, int y2, int bX, int bY, int eX, int eY) {
        if (x < bX || x > eX) return false;
        int y = yOfX(x, bX, bY, eX, eY);
        if (y1 < y2) {
            return y1 < y && y < y2;
        } else {
            return y2 < y && y < y1;
        }

    }

}
