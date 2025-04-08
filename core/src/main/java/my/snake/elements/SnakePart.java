package my.snake.elements;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;

public class SnakePart {
    public int x;
    public int y;
    public int rotation;

    protected SnakePart nextPart;

    public SnakePart(int x, int y, int rotation, SnakePart nextPart) {
        this.x = x;
        this.y = y;
        this.rotation = rotation;
        this.nextPart = nextPart;
    }

    public void add() {
        if(nextPart!=null) {
            nextPart.add();
        } else {
            nextPart = new SnakePart((int) (x + MathUtils.cosDeg(rotation)), (int) (y + MathUtils.sinDeg(rotation)), rotation, null);
        }
    }

//    public void move() {
//        if(nextPart!=null) nextPart.move(x, y, rotation);
//    }

    public void draw(GridDrawer gridDrawer, TextureRegion partTexture, TextureRegion tailTexture) {
        if(nextPart!=null) {
            gridDrawer.drawCell(partTexture, x, y, rotation);
            nextPart.draw(gridDrawer, partTexture, tailTexture);
        } else {
            gridDrawer.drawCell(tailTexture, x, y, rotation);
        }
    }

    protected void move(int x, int y, int rotation) {
        if(nextPart!=null) {
            nextPart.move(this.x, this.y, this.rotation);
        }
        this.x = x;
        this.y = y;
        this.rotation = rotation;
    }
}
