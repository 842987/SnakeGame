package my.snake.elements;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;

public class Snake extends SnakePart {

    GridDrawer gridDrawer;

    TextureRegion headTexture;
    TextureRegion partTexture;
    TextureRegion tailTexture;

    public Snake(int x, int y, int rotation, GridDrawer gridDrawer, TextureRegion headTexture, TextureRegion partTexture, TextureRegion tailTexture) {
        super(x, y, rotation, null);
        add();

        this.gridDrawer = gridDrawer;
        this.headTexture = headTexture;
        this.partTexture = partTexture;
        this.tailTexture = tailTexture;
    }

    public void move(int rotation) {
        if(this.rotation != rotation-180 && this.rotation != rotation+180) {
            this.rotation = rotation;
        }
        nextPart.move(x, y, rotation);
        x += (int) MathUtils.cosDeg(this.rotation);
        y += (int) MathUtils.sinDeg(this.rotation);
    }

    public void draw() {
        gridDrawer.drawCell(headTexture, x, y, rotation);
        nextPart.draw(gridDrawer, partTexture, tailTexture);
    }
}
