package music;

import reaction.Gesture;
import reaction.Reaction;

import java.awt.*;

public class Rest extends Duration {
    public Staff staff;
    public Time time;
    public int line = 4; // default value

    public Rest(Staff staff, Time time) {
        this.staff = staff;
        this.time = time;
        addReaction(new Reaction("E-E") {

            public int bid(Gesture g) { // add a flag
                int y = g.vs.yM(), x1 = g.vs.xL(), x2 = g.vs.xH(), x = Rest.this.time.x;
                if(x1 > x || x2 < x) { return UC.NO_BID; }
                return Math.abs(y - Rest.this.staff.yLine(4)); // 4 is the default line
            }

            public void act(Gesture g) {
                Rest.this.incFlag();
            }
        });

        addReaction(new Reaction("W-W") {

            public int bid(Gesture g) { // remove a flag
                int y = g.vs.yM(), x1 = g.vs.xL(), x2 = g.vs.xH(), x = Rest.this.time.x;
                if(x1 > x || x2 < x) { return UC.NO_BID; }
                return Math.abs(y - Rest.this.staff.yLine(4)); // 4 is the default line
            }

            public void act(Gesture g) {
                Rest.this.decFlag();
            }
        });
    }

    public int y() { return staff.yLine(line); }

    @Override
    public void show(Graphics g) {
        int h = staff.H(), y = y();
        if(nFlag == -2) { Glyph.REST_W.showAt(g, h, time.x, y); }
        if(nFlag == -1) { Glyph.REST_H.showAt(g, h, time.x, y); }
        if(nFlag == 0) { Glyph.REST_Q.showAt(g, h, time.x, y); }
        if(nFlag == 1) { Glyph.REST_1F.showAt(g, h, time.x, y); }
        if(nFlag == 2) { Glyph.REST_2F.showAt(g, h, time.x, y); }
        if(nFlag == 3) { Glyph.REST_3F.showAt(g, h, time.x, y); }
        if(nFlag == 4) { Glyph.REST_4F.showAt(g, h, time.x, y); }
    }

}
