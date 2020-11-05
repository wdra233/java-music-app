package music;

import java.util.ArrayList;

public class Time {
    public int x;

    private Time(int x, List list) {
        this.x = x;
        list.add(this);
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
