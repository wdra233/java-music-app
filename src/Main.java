import graphicsLib.Window;
import reaction.ShapeTrainerApp;
import sandbox.PaintInk;
import sandbox.ReactionTest;

public class Main {
    public static void main(String[] args) {
//        Window.PANEL = new Paint();
//        Window.PANEL = new Squares();
//        Window.PANEL = new PaintInk();
//        Window.PANEL = new ShapeTrainerApp();
        Window.PANEL = new ReactionTest();
        Window.launch();
    }
}
