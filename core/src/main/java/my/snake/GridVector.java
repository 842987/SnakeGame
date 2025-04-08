package my.snake;

public class GridVector {
    public int x;
    public int y;

    public GridVector(int x, int y) {
        set(x, y);
    }

    public void set(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
