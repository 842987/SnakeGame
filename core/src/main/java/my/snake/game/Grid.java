package my.snake.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import my.snake.elements.GridDrawer;
import my.snake.elements.Snake2;

public class Grid extends Actor {
//    int cellSize;

    long updatePeriod = 450;
    long lastUpdate;

//    int width = 25;
//    int height = 25;

    TextureRegion mandarinTexture;
    Array<GridPoint2> mandarins;

//    Viewport viewport;
//    OrthographicCamera camera;
    GridDrawer gridDrawer;

    Snake2 snake;

    int snakeSpeedRotation;

    public Grid(Skin skin, int gridWidth, int gridHeight, int snakeX, int snakeY, int snakeSpeedRotation) {
//        camera = new OrthographicCamera();

//        Skin skin = snakeGame.getSkin();
//        spriteBatch = snakeGame.getSpriteBatch();

        TextureRegion cellTexture = skin.getRegion("cell");
//        cellSize = cellTexture.getRegionHeight();
        gridDrawer = new GridDrawer(cellTexture, gridWidth, gridHeight, cellTexture.getRegionWidth(), cellTexture.getRegionHeight());

//        viewport = new FitViewport(750, 500);

//        mandarins.add(new GridPoint2(3, 3));

//        TextureRegion snakeHeadTexture = skin.getRegion("snakeHead");
//        TextureRegion snakeBodyTexture = skin.getRegion("snakeBody");
//        TextureRegion snakeTailTexture = skin.getRegion("snakeTail");

        this.snakeSpeedRotation = snakeSpeedRotation;

        snake = new Snake2(
            snakeX, snakeY, this.snakeSpeedRotation,
            gridDrawer,
            skin.getRegion("snakeHead"), skin.getRegion("snakeBody"), skin.getRegion("snakeTail")
        );

        mandarinTexture = skin.getRegion("mandarin");
        mandarins = new Array<>();
        mandarins.add(createMandarin());

        lastUpdate = System.currentTimeMillis();
    }

    public void reset(int gridWidth, int gridHeight, int snakeX, int snakeY, int snakeSpeedRotation) {
        gridDrawer.setGridSize(gridWidth, gridHeight);
        this.snakeSpeedRotation = snakeSpeedRotation;
        Snake2.SnakePart head = snake.getBody().get(0);
        head.x = snakeX;
        head.y = snakeY;
        head.rotation = snakeSpeedRotation;
        snake.getBody().clear();
        snake.getBody().add(head);
        snake.add();
    }

    @Override
    protected void sizeChanged() {
//        if(getWidth()==0&&getHeight()==0)return;
        gridDrawer.setSize(getWidth(), getHeight());
//        viewport.update((int) getWidth(), (int) getHeight());
//        System.out.println(getWidth() + ";" + getHeight());
    }

    @Override
    protected void positionChanged() {
        gridDrawer.setPosition(getX(), getY());
    }

    //    public void setGridSize(float width, float height) {
//        gridDrawer.setSize(width, height);
//    }


    public GridPoint2 createMandarin() {
        Array<GridPoint2> clearCells = snake.getClearCells(gridDrawer.getGridWidth(), gridDrawer.getGridHeight());
        while (true) {
            for (GridPoint2 i : clearCells) {
                if (MathUtils.randomBoolean(0.01f)) {
                    return i;
                }
            }
        }
    }

//    @Override
//    public void act(float delta) {
//        super.act(delta);
//    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
//        super.draw(batch, parentAlpha);

        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
//            snakeSpeed.setAngleDeg(90);
            snakeSpeedRotation = 90;
//            snakeSpeed.set(0, 1);
        } else if (Gdx.input.isKeyPressed(Input.Keys.S)) {
//            snakeSpeed.setAngleDeg(270);
            snakeSpeedRotation = 270;
//            snakeSpeed.set(0, -1);
        } else if (Gdx.input.isKeyPressed(Input.Keys.A)) {
//            snakeSpeed.setAngleDeg(180);
            snakeSpeedRotation = 180;
//            snakeSpeed.set(-1, 0);
        } else if (Gdx.input.isKeyPressed(Input.Keys.D)) {
//            snakeSpeed.setAngleDeg(180);
            snakeSpeedRotation = 0;
//            snakeSpeed.set(1, 0);
        }

//        viewport.apply(true);
//        batch.setProjectionMatrix(viewport.getCamera().combined);
        gridDrawer.begin(batch);
        if (updatePeriod <= System.currentTimeMillis() - lastUpdate) {
            snake.move(snakeSpeedRotation);
            if (snake.isHeadOutside(gridDrawer.getGridWidth(), gridDrawer.getGridHeight()) || snake.isHeadOverBody()) {
                System.out.println(snake.getHeadX() + ";" + snake.getHeadY());
                snake.draw();
                updatePeriod = 10000;
//                reset(gridDrawer.getGridWidth(), gridDrawer.getGridHeight(), 0, 0, 90);
            } else lastUpdate = System.currentTimeMillis();
        }
        for (GridPoint2 mandarin : mandarins) {
            if (snake.isHeadOver(mandarin.x, mandarin.y)) {
                mandarins.removeValue(mandarin, true);
                snake.add();
                mandarins.add(createMandarin());
                updatePeriod -= 10;
            } else gridDrawer.drawCell(mandarinTexture, mandarin.x, mandarin.y, 0);
        }
        snake.draw();
//        getStage().getViewport().apply(true);
    }
}
