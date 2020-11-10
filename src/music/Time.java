package music;

import java.util.ArrayList;

public class Time {
    public int x;
    public Head.List heads = new Head.List();

    private Time(int x, List list) {
        this.x = x;
        list.add(this);
    }

    public void unStemHead(int y1, int y2) {
        for (Head h : heads) {
            int y = h.y();
            if (y > y1 && y < y2) { h.unStem(); }
        }
    }

    public void stemHead(Staff staff, boolean up, int y1, int y2) {
        Stem s = new Stem(staff, up);
        for (Head h : heads) {
            int y = h.y();
            if (y > y1 && y < y2) { h.joinStem(s); }
        }

    }

    //-------------------------subclass list-----------------------------------------
    public static class List extends ArrayList<Time> {
        public Sys sys;
        public List(Sys sys) {
            this.sys = sys;
        }


        public Time getTime(int x) {
            // find or create a time value
            Time res = null;
            int dis = UC.SNAP_TIME;
            for(Time time : this) {
                int d = Math.abs(x - time.x);
                if(d < dis) {
                    dis = d;
                    res = time;
                }
            }
            return ( res == null ) ? new Time(x, this) : res;
        }
    }
}
