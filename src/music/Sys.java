package music;

import reaction.Gesture;
import reaction.Mass;
import reaction.Reaction;

import java.awt.*;
import java.util.ArrayList;

public class Sys extends Mass {

    public ArrayList<Staff> staffs = new ArrayList<>();
    public Page page;
    public int iSys;
    public Time.List times;
    public Stem.List stems = new Stem.List();

    public Sys(Page page, int iSys) {
        super("BACK");
        this.page = page;
        this.iSys = iSys;
        times = new Time.List(this);

        addReaction(new Reaction("E-E") {
            @Override
            public int bid(Gesture g) {
                int x1 = g.vs.xL(), y1 = g.vs.yL(), x2 = g.vs.xH(), y2 = g.vs.yH();
                if(stems.fastReject(y1, y2)) { return UC.NO_BID; }
                Stem.List tmpStems = stems.allInterceptor(x1, y1, x2, y2);
                if(tmpStems.size() < 2) { return UC.NO_BID; }
                Beam b = tmpStems.get(0).beam;
                for(Stem s : tmpStems) { if(s.beam != b) { return UC.NO_BID; } }
                if(b == null && tmpStems.size() != 2) { return UC.NO_BID; }
                if(b == null && (tmpStems.get(0).nFlag != 0 || tmpStems.get(1).nFlag != 0)) { return UC.NO_BID; }
                return 50;
            }

            @Override
            public void act(Gesture g) {
                int x1 = g.vs.xL(), y1 = g.vs.yL(), x2 = g.vs.xH(), y2 = g.vs.yH();
                Stem.List tmpStems = stems.allInterceptor(x1, y1, x2, y2);
                Beam b = tmpStems.get(0).beam;
                if(b == null) {
                    new Beam(tmpStems.get(0), tmpStems.get(1));
                } else {
                    for (Stem s : tmpStems) {
                        s.incFlag();
                    }
                }
            }
        });
    }

    public Time getTime(int x) { return times.getTime(x); }

    public int yTop() { return page.sysTop(iSys); }

    public int yBot() { return staffs.get(staffs.size() - 1).yBot(); }

    public void addNewStaff(int iStaff) { staffs.add(new Staff(this, iStaff)); }

    // ------------ Sys Format---------------
    public static class Fmt extends ArrayList<Staff.FMT> {
        public int maxH = 8;

        public ArrayList<Integer> staffOffsets = new ArrayList<>();

        public void addNew(int yOff) {
            add(new Staff.FMT());
            staffOffsets.add(yOff);
        }

        public int height() {
            int last = size() - 1;
            return get(last).height() + staffOffsets.get(last);
        }

        public void showAt(Graphics g, int y, Page page) {
            for(int i = 0; i < size(); i++) { get(i).showAt(g, y + staffOffsets.get(i), page); }

            int x1 = page.xMargin.lo, x2 = page.xMargin.hi, y2 = y + height();
            g.drawLine(x1, y, x1, y2);
            g.drawLine(x2, y, x2, y2);
        }

    }
}
