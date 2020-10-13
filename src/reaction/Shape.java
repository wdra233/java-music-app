package reaction;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import graphicsLib.G;
import music.I;
import music.UC;
import reaction.Shape.Prototype.List;

public class Shape implements Serializable {
    public Prototype.List prototypes = new List();
    public String name;
    public Shape(String name) { this.name = name; }
    public static String filename = "ShapeDB.dat";
    public static HashMap<String, Shape> DB = loadShapeDB();
    public static Shape DOT = DB.get("DOT");
    public static Collection<Shape> LIST = DB.values();

    public static void saveShapeDB() {
        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename));
            oos.writeObject(DB);
            System.out.println("saved " + filename);
            oos.close();
        } catch (Exception e) {
            System.out.println("Failed database save");
            System.out.println(e);
        }

    }

    public static HashMap<String, Shape> loadShapeDB() {
        HashMap<String, Shape> res = new HashMap<>();
        res.put("DOT", new Shape("DOT"));
        try {
            System.out.println("Attempting DB Load...");
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename));
            res = (HashMap<String, Shape>) ois.readObject();
            System.out.println("Successful load " + res.keySet());
            ois.close();
        } catch (Exception e) {
            System.out.println("Database Load Failed");
            e.printStackTrace();
            System.out.println(e);
        }
        return res;
    }

    public static Shape getOrCreate(String name) {
        if (!DB.containsKey(name)) { DB.put(name, new Shape(name)); }
        return DB.get(name);

    }

    public static Shape recognize(Ink ink) {
        if (ink.vs.size.x < UC.DOT_THRESHOLD && ink.vs.size.y < UC.DOT_THRESHOLD) { return DOT; }
        Shape bestMatch = null; int bestSoFar = UC.NO_MATCH_DIST;
        for(Shape s : LIST) {
            int d = s.prototypes.bestDist(ink.norm);
            if(d < bestSoFar) { bestMatch = s; bestSoFar = d; }
        }
        return bestMatch;

    }

    // --------------------------------------------Prototype---------------------------------------------------
    public static class Prototype extends Ink.Norm implements Serializable {
        int nBlend = 1;

        public Prototype(Ink.Norm norm) { super(norm); }

        public void blend(Ink.Norm norm) {
            blend(norm, nBlend);
            nBlend++;
        }
        // ----------------------------------------List--------------------------------------------------
        public static class List extends ArrayList<Prototype> implements Serializable {
            public static Prototype bestMatch;
            private static int m = UC.PROTOTYPE_LIST_MARGIN,  w = UC.PROTOTYPE_LIST_SIZE;
            private static G.VS showBox = new G.VS(m, m, w, w);

            public int bestDist(Ink.Norm norm) {
                bestMatch = null;
                int bestSoFar = UC.NO_MATCH_DIST;
                for(Prototype p : this) {
                    int d = p.dist(norm);
                    if(d < bestSoFar) {
                        bestMatch = p;
                        bestSoFar = d;
                    }
                }
                return bestSoFar;
            }

            // tells us which prototype we landed on
            public int nHit(int x) { return x / (m + w); }

            public void show(Graphics g) {
                g.setColor(Color.ORANGE);
                for(int i = 0; i < size(); i++) {
                    Prototype p = get(i);
                    int x = m + i * (m + w);
                    showBox.loc.set(x, m);
                    p.drawAt(g, showBox);
                    g.drawString("" + p.nBlend, x, 20);
                }
            }

            public Prototype blendOrAdd(Ink.Norm norm) {
                int d = bestDist(norm);
                if (d < UC.NO_MATCH_DIST) { bestMatch.blend(norm); return bestMatch; }
                Prototype res = new Prototype(norm);
                add(res);
                return res;
            }


        }

    }
    // -------------------------------------Trainer----------------------------------
    public static class Trainer implements I.Area {
        public NameState ns = new NameState();
        public static boolean isTraining = false;

        @Override
        public boolean hit(int x, int y) { return isTraining; }

        @Override
        public void dn(int x, int y) { Ink.BUFFER.dn(x, y); }

        @Override
        public void drag(int x, int y) { Ink.BUFFER.drag(x, y);

        }

        @Override
        public void up(int x, int y) {
            if (ns.illegal()) { ns.shapeBeingTrained = null; return; }
            Ink ink = new Ink();
            ns.shapeBeingTrained = Shape.getOrCreate(ns.name);
            Prototype.List pList = ns.shapeBeingTrained.prototypes;
            if (pList.size() > 0 && y < UC.PROTOTYPE_LIST_Y_LIMIT) {
                int iProto = pList.nHit(x);
                if (iProto < pList.size()) { pList.remove(iProto); }
            } else {
                pList.blendOrAdd(ink.norm);
            }
            ns.setState();
        }


        public void show(Graphics g) {
            g.setColor(UC.SHAPE_TRAINER_BACKGROUND_COLOR); g.fillRect(0, 0, 5000, 5000);
            g.setColor(Color.BLACK);
            Ink.BUFFER.show(g);
            ns.show(g);
        }

        public void keyTyped(char c) { ns.keyTyped(c); }

        // ------------------------NameState-----------------------------------------
        public static class NameState {
            public static String
                    UNKNOWN = "<-This name is currently unknown", ILLEGAL = "<-illegal>", KNOWN = "<-known",
            INSTRUCTIONS = "type name and draw example - space clears name - enter saves db";
            public String name = "";
            public String state = ILLEGAL;
            public Shape shapeBeingTrained;


            public void show(Graphics g) {
                g.setColor(Color.BLACK);
                g.drawString(INSTRUCTIONS, 20, 100);
                if (state.equals(ILLEGAL)) { g.setColor(Color.RED); }
                g.drawString(name, 600, 30);
                g.drawString(state, 700, 30);
                if(shapeBeingTrained != null) { shapeBeingTrained.prototypes.show(g); }
            }

            public void setState() {
                shapeBeingTrained = null;
                state = (name.equals("") || name.equals("DOT")) ? ILLEGAL : UNKNOWN;
                if (state == UNKNOWN) {
                    if (Shape.DB.containsKey(name)) {
                        state = KNOWN;
                        shapeBeingTrained = Shape.DB.get(name);
                    }
                }
            }

            public boolean illegal() { return state == ILLEGAL; }

            public void keyTyped(char c) {
                System.out.println("typed: " + c);
                name = (c == ' ' || c == 0x0D || c == 0x0A) ? "" : name + c;
                setState();
                if (c == 0x0D || c == 0x0A) {
                    Shape.saveShapeDB();
                }
            }

        }

    }
}
