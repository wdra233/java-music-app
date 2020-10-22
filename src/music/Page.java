package music;

import graphicsLib.G;
import reaction.Gesture;
import reaction.Mass;
import reaction.Reaction;

import java.awt.*;
import java.util.ArrayList;

public class Page extends Mass {

    public static Page PAGE;
    public G.LoHi xMargin, yMargin;
    public Sys.Fmt sysFmt = new Sys.Fmt();
    public int sysGap; // Size of space between 2 systems
    public ArrayList<Sys> sysList = new ArrayList<>();
    public static Reaction R1;

    public Page(int y) {
        super("BACK");
        PAGE = this;
        int mm = 50;
        xMargin = new G.LoHi(mm, UC.WINDOW_WIDTH - mm);
        yMargin = new G.LoHi(y, UC.WINDOW_HEIGHT - mm);

        // TODO: create two reactions
        Reaction.initialReaction.get(0).disable();
        addNewStaffFmtToSysFmt(y);
        addNewSys();

        addReaction(R1 = new Reaction("E-W") { // add a new staff to aa growing sysFmt
            public int bid(Gesture g) {
                return (g.vs.yM() < PAGE.allSysBot()) ? UC.NO_BID : 0;
            }

            public void act(Gesture g) {
                addNewStaffFmtToSysFmt(g.vs.yM());
            }
        });

        addReaction(new Reaction("W-W") { // add a new system

            public int bid(Gesture g) {
                return (g.vs.yM() < PAGE.allSysBot()) ? UC.NO_BID : 0;
            }

            public void act(Gesture g) {
                if (PAGE.sysList.size() == 1) {
                    PAGE.sysGap = g.vs.yM() - PAGE.allSysBot();
                    R1.disable();
                }
                addNewSys();
            }
        });
    }

    @Override
    public void show(Graphics g) {
        g.setColor(Color.RED);
        g.drawLine(0, yMargin.lo, 30, yMargin.lo);
        for (int i = 0; i < sysList.size(); i++) {
            sysFmt.showAt(g, sysTop(i), this);
        }
    }

    public void addNewStaffFmtToSysFmt(int y) {
        int yOff = y - yMargin.lo;
        int iStaff = sysFmt.size();
        sysFmt.addNew(yOff);
        for (Sys sys : sysList) { // upgrade all systems to match sys format
            sys.addNewStaff(iStaff);
        }
    }

    public void addNewSys() {
        Sys sys = new Sys(this, sysList.size());
        sysList.add(sys);
        for(int i = 0; i < sysFmt.size(); i++) {
            sys.addNewStaff(i);
        }
    }

    public int sysTop(int iSys) { return yMargin.lo + iSys * (sysFmt.height() + sysGap); }

    public int allSysBot() {
        int n = sysList.size();
        return yMargin.lo + n * sysFmt.height() + (n - 1) * sysGap;
    }

}
