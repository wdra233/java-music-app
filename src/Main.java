import graphicsLib.Window;
import sandbox.Paint;
import sandbox.Squares;

public class Main {
    public static void main(String[] args) {
//        Window.PANEL = new Paint();

        Window.PANEL = new Squares();
        Window.launch();
    }
}
