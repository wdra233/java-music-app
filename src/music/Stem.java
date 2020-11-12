package music;

import java.awt.*;
import java.util.Collections;

public class Stem extends Duration {
    public Staff staff;
    public Head.List heads = new Head.List();
    boolean isUp = true;

    public Stem(Staff staff, boolean up) {
        super();
        this.staff = staff;
        isUp = up;
    }

    @Override
    public void show(Graphics g) {
        if (nFlag >= -1 && heads.size() > 0) {
            int x = x(), yH = yFirstHead(), yB = yBeamEnd();
            g.drawLine(x, yH, x, yB);
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
}
