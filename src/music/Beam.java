package music;

import reaction.Mass;

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
