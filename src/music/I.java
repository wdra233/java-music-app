package music;

public interface I {
    public interface Hit { public boolean hit(int x, int y); }
    public interface Area extends Hit {
        public void dn(int x, int y);
        public void drag(int x, int y);
        public void up(int x, int y);
    }
}
