package music;

import reaction.Mass;

import java.awt.*;

public class Staff extends Mass {

    public Sys sys;
    public int iStaff;
    public Staff.FMT fmt;

    public Staff(Sys sys, int iStaff) {
        super("BACK");
        this.sys = sys;
        this.iStaff = iStaff;
        fmt = sys.page.sysFmt.get(iStaff);
    }

    public int yTop() { return sys.yTop() + sys.page.sysFmt.staffOffsets.get(iStaff); }

    // --- Staff Format
    public static class FMT {
        public int nLines = 5;
        public int H = 8; // 1/2 of the spacing between two lines

        public int height() { return 2 * H * (nLines - 1); }

        public void showAt(Graphics g, int y, Page page) {
            g.setColor(Color.GRAY);

            int x1 = page.xMargin.lo, x2 = page.xMargin.hi, h = 2 * H;
            for(int i = 0; i < nLines; i++) {
                g.drawLine(x1, y, x2, y);
                y += h;
            }
        }
    }

}
