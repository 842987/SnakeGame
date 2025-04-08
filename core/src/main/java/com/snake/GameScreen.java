package com.snake;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * First screen of the application. Displayed after the application is created.
 */
public class GameScreen implements Screen {

    Viewport viewport;
    SpriteBatch spriteBatch;

    TextureRegion mandarinTexture;
    Array<GridPoint2> mandarins;

    GridDrawer gridDrawer;

    Snake snake;
    int snakeSpeedRotation;

    Stage stage;
    Window gameEndWindow;
    Label mandarinsLabel;
    Label mandarinsRecordLabel;
    Label resultsLabel;

    long updatePeriod = 450;
    long lastUpdate = -1;

    long startTime = -1;
    float endTime = -1;

    int mandarinsRecord = 0;
    float mandarinsRecordTime = 0;

    public GameScreen(SnakeGame snakeGame) {

        Skin skin = snakeGame.getSkin();
        spriteBatch = snakeGame.getSpriteBatch();

        TextureRegion cellTexture = skin.getRegion("cell");
        int cellSize = cellTexture.getRegionHeight();
        gridDrawer = new GridDrawer(cellTexture, 10, 10, cellSize, cellSize);

        viewport = new FitViewport(gridDrawer.getWidth(), gridDrawer.getHeight());
//        viewport = new FitViewport(750, 500);

        mandarinTexture = skin.getRegion("mandarin");
        mandarins = new Array<>();
        mandarins.add(new GridPoint2(2, 2));

        TextureRegion snakeHeadTexture = skin.getRegion("snakeHead");
        TextureRegion snakeBodyTexture = skin.getRegion("snakeBody");
        TextureRegion snakeTailTexture = skin.getRegion("snakeTail");

        snakeSpeedRotation = 90;

        snake = new Snake(
            gridDrawer.getGridWidth() / 2 - 1, gridDrawer.getGridHeight() / 2 - 1, snakeSpeedRotation,
            gridDrawer,
            snakeHeadTexture, snakeBodyTexture, snakeTailTexture
        );

//        lastUpdate = System.currentTimeMillis();

        stage = new Stage(viewport);
        Gdx.input.setInputProcessor(stage);

        gameEndWindow = new Window("Game is over", skin);
        gameEndWindow.setVisible(false);
        gameEndWindow.setMovable(true);
        gameEndWindow.setResizable(true);
        gameEndWindow.setSize(175, 150);
        stage.addActor(gameEndWindow);

//        Label gameOverLabel = new Label("Game is over", skin);
//        gameEndWindow.add(gameOverLabel).pad(3.5f).row();
        mandarinsLabel = new Label("You ate a lot of mandarins", skin);
        gameEndWindow.add(mandarinsLabel).row();
        resultsLabel = new Label("And crashed into something", skin);
        gameEndWindow.add(resultsLabel).row();
        mandarinsRecordLabel = new Label("Last record: 0 mandarins in 0 seconds", skin);
        gameEndWindow.add(mandarinsRecordLabel).row();

        HorizontalGroup widthGroup = new HorizontalGroup();
        Label gridWidthLabel = new Label("Grid width: ", skin);
        widthGroup.addActor(gridWidthLabel);
        TextField gridWidthTextField = new TextField(String.valueOf(gridDrawer.getGridWidth()), skin);
        widthGroup.addActor(gridWidthTextField);

        gameEndWindow.add(widthGroup).row();

        HorizontalGroup heightGroup = new HorizontalGroup();
        Label gridHeightLabel = new Label("Grid height: ", skin);
        heightGroup.addActor(gridHeightLabel);
        TextField gridHeightTextField = new TextField(String.valueOf(gridDrawer.getGridHeight()), skin);
        heightGroup.addActor(gridHeightTextField);

        gameEndWindow.add(heightGroup).padBottom(3.5f).row();

        TextButton restartButton = new TextButton("Restart", skin);
        restartButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                int gridWidth = 0;
                int gridHeight = 0;

                boolean isValid = true;

                try {
                    gridWidth = Integer.parseInt(gridWidthTextField.getText());
                } catch (NumberFormatException exception) {
                    gridWidthTextField.setText("Enter valid value");
                    isValid = false;
                }

                try {
                    gridHeight = Integer.parseInt(gridHeightTextField.getText());
                } catch (NumberFormatException exception) {
                    gridHeightTextField.setText("Enter valid value");
                    isValid = false;
                }


                if (gridWidth < 3) {
                    gridWidthTextField.setText("Enter valid value");
                    isValid = false;
                }
                if (gridHeight < 3) {
                    gridHeightTextField.setText("Enter valid value");
                    isValid = false;
                }

                if (!isValid) return;

                gridDrawer.setGridSize(gridWidth, gridHeight);

                stage.unfocus(gridWidthTextField);
                stage.unfocus(gridHeightTextField);

                int mandarinsCount = snake.getBody().size - 2;
                if (mandarinsCount > mandarinsRecord) {
                    mandarinsRecord = mandarinsCount;
                    mandarinsRecordTime = endTime;
                }
                mandarins.clear();
                mandarins.add(new GridPoint2(2, 2));

                snake.getBody().clear();
                snake.addHead(gridDrawer.getGridWidth() / 2 - 1, gridDrawer.getGridHeight() / 2 - 1, 90);

                updatePeriod = 450;
                startTime = -1;

                snakeSpeedRotation = 90;
                stop = false;
                gameEndWindow.setVisible(false);
            }
        });
        gameEndWindow.add(restartButton).row();

        gameEndWindow.pack();
        gameEndWindow.setPosition(stage.getWidth() / 2.0f - gameEndWindow.getWidth() / 2.0f, stage.getHeight() / 2.0f - gameEndWindow.getHeight() / 2.0f);
    }

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

    //    int cameraSpeed = 150;
    boolean stop = false;

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.WHITE);

        if (lastUpdate == -1) {
            lastUpdate = System.currentTimeMillis();
        }

        if (startTime == -1) {
            startTime = System.currentTimeMillis();
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE) && !gameEndWindow.isVisible()) {
            stop = !stop;
        }

//        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
//            viewport.getCamera().translate(0, cameraSpeed * delta, 0);
//            viewport.getCamera().update();
//        } else if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
//            viewport.getCamera().translate(0, -cameraSpeed * delta, 0);
//            viewport.getCamera().update();
//        } else if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
//            viewport.getCamera().translate(-cameraSpeed * delta, 0, 0);
//            viewport.getCamera().update();
//        } else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
//            viewport.getCamera().translate(cameraSpeed * delta, 0,0);
//            viewport.getCamera().update();
//        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.W)) {
            snakeSpeedRotation = 90;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.S)) {
            snakeSpeedRotation = 270;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.A)) {
            snakeSpeedRotation = 180;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.D)) {
            snakeSpeedRotation = 0;
        }

        spriteBatch.setProjectionMatrix(viewport.getCamera().combined);
        spriteBatch.begin();
        gridDrawer.begin(spriteBatch);
        if (updatePeriod <= System.currentTimeMillis() - lastUpdate && !stop) {
            snake.move(snakeSpeedRotation);
            if (snake.isHeadOutside(gridDrawer.getGridWidth(), gridDrawer.getGridHeight()) || snake.isHeadOverBody()) {
                endTime = (System.currentTimeMillis() - startTime) / 1000.0f;
                if (snake.getBody().size == 3) {
                    mandarinsLabel.setText("You ate 1 mandarin" + " in " + endTime + " seconds");
                } else {
                    mandarinsLabel.setText("You ate " + (snake.getBody().size - 2) + " mandarins" + " in " + endTime + " seconds");
                }

                mandarinsRecordLabel.setText("Last record: " + mandarinsRecord + " mandarins in " + mandarinsRecordTime + " seconds");

                if (snake.isHeadOverBody()) {
                    resultsLabel.setText("And crashed into yourself");
                } else {
                    resultsLabel.setText("And crashed into a wall");
                }

                stop = true;
                gameEndWindow.pack();
                gameEndWindow.setVisible(true);
            }
            lastUpdate = System.currentTimeMillis();
        }
        for (GridPoint2 mandarin : mandarins) {
            if (snake.isHeadOver(mandarin.x, mandarin.y)) {
                mandarins.removeValue(mandarin, true);
                snake.add();
                mandarins.add(createMandarin());
                updatePeriod -= 10;
            } else {
                gridDrawer.drawCell(mandarinTexture, mandarin.x, mandarin.y, 0);
            }
        }
        snake.draw();
        spriteBatch.end();

        stage.act();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
        viewport.getCamera().position.set(gridDrawer.getX() + gridDrawer.getWidth() / 2.0f, gridDrawer.getY() + gridDrawer.getHeight() / 2.0f, 0);
        viewport.getCamera().update();
    }

    @Override
    public void show() {
    }

    @Override
    public void pause() {
        stop = true;
    }

    @Override
    public void resume() {
        stop = false;
    }

    @Override
    public void hide() {
        stop = true;
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
