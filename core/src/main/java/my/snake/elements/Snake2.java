package my.snake.elements;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;

public class Snake2 {
    public class SnakePart {
        public int x;
        public int y;
        public int rotation;

        public SnakePart(int x, int y, int rotation) {
            set(x, y, rotation);
        }

        public void set(int x, int y, int rotation) {
            this.x = x;
            this.y = y;
            this.rotation = rotation;
        }

        public void set(SnakePart snakePart) {
            set(snakePart.x, snakePart.y, snakePart.rotation);
        }

    }

    Array<SnakePart> body;

    GridDrawer gridDrawer;

    TextureRegion headTexture;
    TextureRegion partTexture;
    TextureRegion tailTexture;

    public Snake2(int headX, int headY, int headRotation, GridDrawer gridDrawer, TextureRegion headTexture, TextureRegion partTexture, TextureRegion tailTexture) {
        body = new Array<>();

        body.add(new SnakePart(headX, headY, headRotation));
        add();

        this.gridDrawer = gridDrawer;

        this.headTexture = headTexture;
        this.partTexture = partTexture;
        this.tailTexture = tailTexture;
    }

//    public void setHeadPosition(int x, int y) {
//        SnakePart snakePart = body.get(0);
//
//        int moveX = snakePart.x-x;
//        int moveY = snakePart.y-y;
//
//        for (int i = body.size-1; i >= 0; i--) {
//            snakePart = body.get(i);
//            snakePart.x = snakePart.x+moveX;
//            snakePart.y = snakePart.y+moveY;
//        }
//    }

    public void move(int rotation) {
        for (int i = body.size-1; i > 0; i--) {
            body.get(i).set(body.get(i-1));
        }

        SnakePart head = body.get(0);

        if (head.rotation != rotation - 180 && head.rotation != rotation + 180) {
            head.rotation = rotation;
        }

        head.x += (int) MathUtils.cosDeg(head.rotation);
        head.y += (int) MathUtils.sinDeg(head.rotation);
    }

    public void add() {
        SnakePart tail = body.get(body.size - 1);
        body.add(new SnakePart(
            tail.x - (int) MathUtils.cosDeg(tail.rotation),
            tail.y - (int) MathUtils.sinDeg(tail.rotation),
            tail.rotation
        ));
    }

    public void draw() {
        SnakePart snakePart = body.get(0);
        gridDrawer.drawCell(headTexture, snakePart.x, snakePart.y, snakePart.rotation);
        for (int i = 1; i < body.size - 1; i++) {
            snakePart = body.get(i);
            gridDrawer.drawCell(partTexture, snakePart.x, snakePart.y, snakePart.rotation);
        }
        snakePart = body.get(body.size - 1);
        gridDrawer.drawCell(tailTexture, snakePart.x, snakePart.y, snakePart.rotation);
    }

    public Array<SnakePart> getBody() {
        return body;
    }

//    public void setHeadX(he) {
//        body.get(0).x = 1;
//    }
//
//    public void setHeadY() {
//        body.get(0).y = ;
//    }
    public void addHead(int headX, int headY, int headRotation) {
        if(body.size != 0) return;

        body.add(new SnakePart(headX, headY, headRotation));
        add();
    }

    public int getHeadX() {
        return body.get(0).x;
    }

    public int getHeadY() {
        return body.get(0).y;
    }

    public boolean isHeadOver(int x, int y) {
        SnakePart head = body.get(0);
        return head.x == x && head.y == y;
    }

    public boolean isHeadOverBody() {
        SnakePart head = body.get(0);
        for(int i = 1; i < body.size; i++) {
            if(head.x == body.get(i).x && head.y == body.get(i).y) {
                return true;
            }
        }
        return false;
    }

    public boolean isHeadOutside(int width, int height) {
        SnakePart head = body.get(0);
        return head.x < 0 || head.x >= width || head.y < 0 || head.y >= height;
    }

//    public boolean isHeadOutsideNextMove(int width, int height) {
//        SnakePart head = body.get(0);
//        return head.x < 0 || head.x >= width || head.y < 0 || head.y >= height;
//    }

    public Array<GridPoint2> getClearCells(int width, int height) {
        Array<GridPoint2> clearCells = new Array<>();
        Array<GridPoint2> snakeCells = new Array<>();

        for(SnakePart i: body) {
            snakeCells.add(new GridPoint2(i.x, i.y));
        }

        for(int i = 0; i < height; i++) {
            for(int j = 0; j < width; j++) {
                clearCells.add(new GridPoint2(j, i));
            }
        }

        clearCells.removeAll(snakeCells, false);
        return clearCells;
    }
}
