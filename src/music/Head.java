package music;

import reaction.Mass;

import java.awt.*;

public class Head extends Mass {

    public Staff staff;
    public int line;
    public Time time;

    public Head(Staff staff, int x, int y) {
        super("NOTE"); // note layer
        this.staff = staff;
        this.time = staff.sys.getTime(x);
        line = staff.lineOfY(y);
    }

    public void show(Graphics g) {
        int h = staff.H();
        Glyph.HEAD_Q.showAt(g, h, time.x, staff.yTop() + line * h);
    }

}
