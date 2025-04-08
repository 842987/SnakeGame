package my.snake.game;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import my.snake.elements.GridDrawer;
import my.snake.elements.Snake2;

/**
 * First screen of the application. Displayed after the application is created.
 */
public class GameScreen implements Screen {

    //    OrthographicCamera camera;
    Viewport viewport;

    SpriteBatch spriteBatch;

    //    TextureRegion cellTexture;
    int cellSize;

    long updatePeriod = 450;
    //    long updateDelta = 0;
    long lastUpdate;

//    int width = 5;
//    int height = 5;

    TextureRegion mandarinTexture;
    Array<GridPoint2> mandarins;

//    TextureRegion snakeHeadTexture;
//    TextureRegion snakeBodyTexture;
//    TextureRegion snakeTailTexture;

    InputProcessor controller = new InputProcessor() {
        @Override
        public boolean keyDown(int keycode) {
            return false;
        }

        @Override
        public boolean keyUp(int keycode) {
            return false;
        }

        @Override
        public boolean keyTyped(char character) {
            return false;
        }

        @Override
        public boolean touchDown(int screenX, int screenY, int pointer, int button) {
            return false;
        }

        @Override
        public boolean touchUp(int screenX, int screenY, int pointer, int button) {
            return false;
        }

        @Override
        public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {
            return false;
        }

        @Override
        public boolean touchDragged(int screenX, int screenY, int pointer) {
            return false;
        }

        @Override
        public boolean mouseMoved(int screenX, int screenY) {
            return false;
        }

        @Override
        public boolean scrolled(float amountX, float amountY) {
            return false;
        }
    };

    GridDrawer gridDrawer;

    Snake2 snake;

    int snakeSpeedRotation;

    Stage stage;

    Window gameEndWindow;
    Label mandarinsLabel;
    Label mandarinsRecordLabel;
    Label resultsLabel;
    TextField gridWidthTextField;
    TextField gridHeightTextField;

    long startTime = -1;
    float endTime = -1;

    int mandarinsRecord = 0;
    float mandarinsRecordTime = 0;

    public GameScreen(SnakeGame snakeGame) {

        Skin skin = snakeGame.getSkin();
        spriteBatch = snakeGame.getSpriteBatch();

        TextureRegion cellTexture = skin.getRegion("cell");
        cellSize = cellTexture.getRegionHeight();
        gridDrawer = new GridDrawer(cellTexture, 10, 10, cellSize, cellSize);

//        camera = new OrthographicCamera();
//        camera.setToOrtho(false, gridDrawer.getWidth(), gridDrawer.getHeight());
        viewport = new FitViewport(gridDrawer.getWidth(), gridDrawer.getHeight());

        mandarinTexture = skin.getRegion("mandarin");
        mandarins = new Array<>();
        mandarins.add(new GridPoint2(3, 3));

        TextureRegion snakeHeadTexture = skin.getRegion("snakeHead");
        TextureRegion snakeBodyTexture = skin.getRegion("snakeBody");
        TextureRegion snakeTailTexture = skin.getRegion("snakeTail");

        snakeSpeedRotation = 90;

        snake = new Snake2(
            gridDrawer.getGridWidth() / 2, gridDrawer.getGridHeight() / 2, snakeSpeedRotation,
            gridDrawer,
            snakeHeadTexture, snakeBodyTexture, snakeTailTexture
        );

//        camera.zoom = 1.5f;
//        camera.update();

        lastUpdate = System.currentTimeMillis();

//        stage = new Stage(new ExtendViewport(750, 500));
        stage = new Stage(viewport);
        Gdx.input.setInputProcessor(stage);

//        Table centerTable = new Table();
//        stage.addActor(centerTable);
//        centerTable.setFillParent(true);

//        Label.LabelStyle labelStyle = new Label.LabelStyle();
//        labelStyle.font = new BitmapFont();
//        labelStyle.fontColor = Color.BLACK;

//        Table informationTable = new Table();
//        centerTable.right().add(informationTable);

//        TextButton pauseButton = new TextButton("Pause", skin);
//        TextButton restartButton = new TextButton("Restart", skin);

//        label = new Label("Test", skin);
//        label.setColor(Color.BLACK);
//        informationTable.add(label).row();
//        centerTable.right().bottom().add(label);
//        label.setText("TestTestTest");
        gameEndWindow = new Window("", skin);
        gameEndWindow.setVisible(false);
        gameEndWindow.setMovable(true);
        gameEndWindow.setResizable(true);
        gameEndWindow.setSize(175, 150);
        stage.addActor(gameEndWindow);

        Label gameOverLabel = new Label("Game is over", skin);
//        gameOverLabel.setFontScale(1.5f);
        gameEndWindow.add(gameOverLabel).pad(3.5f).row();
        mandarinsLabel = new Label("You ate a lot of mandarins", skin);
        gameEndWindow.add(mandarinsLabel).row();
        resultsLabel = new Label("And crashed into something", skin);
        gameEndWindow.add(resultsLabel).row();
        mandarinsRecordLabel = new Label("Last record: 0 mandarins in 0 seconds", skin);
        gameEndWindow.add(mandarinsRecordLabel).row();

        HorizontalGroup widthGroup = new HorizontalGroup();
        Label gridWidthLabel = new Label("Grid width: ", skin);
        widthGroup.addActor(gridWidthLabel);
        gridWidthTextField = new TextField(String.valueOf(gridDrawer.getGridWidth()), skin);
        widthGroup.addActor(gridWidthTextField);

        gameEndWindow.add(widthGroup).row();

        HorizontalGroup heightGroup = new HorizontalGroup();
        Label gridHeightLabel = new Label("Grid height: ", skin);
        heightGroup.addActor(gridHeightLabel);
        gridHeightTextField = new TextField(String.valueOf(gridDrawer.getGridHeight()), skin);
        heightGroup.addActor(gridHeightTextField);

        gameEndWindow.add(heightGroup).row();

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
                mandarins.add(new GridPoint2(3, 3));

                snake.getBody().clear();
                snake.addHead(gridDrawer.getGridWidth() / 2, gridDrawer.getGridHeight() / 2, 90);

                updatePeriod = 450;
                startTime = -1;

                snakeSpeedRotation = 90;
                stop = false;
                gameEndWindow.setVisible(false);
            }
        });
        gameEndWindow.add(restartButton).row();
//        gameEndWindow.setS

        gameEndWindow.pack();
        gameEndWindow.setPosition(stage.getWidth() / 2.0f - gameEndWindow.getWidth() / 2.0f, stage.getHeight() / 2.0f - gameEndWindow.getHeight() / 2.0f);

//        Table buttonTable = new Table();
//        buttonTable.add(pauseButton).row();
//        buttonTable.add(restartButton);
//        informationTable.add(buttonTable);
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

    @Override
    public void show() {

        // Prepare your screen here.
    }

    int cameraSpeed = 150;
    boolean stop = false;

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.WHITE);

        if (startTime == -1) {
            startTime = System.currentTimeMillis();
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE) && !gameEndWindow.isVisible()) {
            stop = !stop;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            viewport.getCamera().translate(0, cameraSpeed * delta, 0);
            viewport.getCamera().update();
        } else if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            viewport.getCamera().translate(0, -cameraSpeed * delta, 0);
            viewport.getCamera().update();
        } else if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            viewport.getCamera().translate(-cameraSpeed * delta, 0, 0);
            viewport.getCamera().update();
        } else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            viewport.getCamera().translate(cameraSpeed * delta, 0,0);
            viewport.getCamera().update();
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.W)) {
//            snakeSpeed.setAngleDeg(90);
            snakeSpeedRotation = 90;
//            snakeSpeed.set(0, 1);
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.S)) {
//            snakeSpeed.setAngleDeg(270);
            snakeSpeedRotation = 270;
//            snakeSpeed.set(0, -1);
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.A)) {
//            snakeSpeed.setAngleDeg(180);
            snakeSpeedRotation = 180;
//            snakeSpeed.set(-1, 0);
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.D)) {
//            snakeSpeed.setAngleDeg(180);
            snakeSpeedRotation = 0;
//            snakeSpeed.set(1, 0);
        }

//        camera.update();

        spriteBatch.setProjectionMatrix(viewport.getCamera().combined);
        spriteBatch.begin();
        gridDrawer.begin(spriteBatch);
        if (updatePeriod <= System.currentTimeMillis() - lastUpdate && !stop) {
            snake.move(snakeSpeedRotation);
            if (snake.isHeadOutside(gridDrawer.getGridWidth(), gridDrawer.getGridHeight()) || snake.isHeadOverBody()) {
//                System.out.println(snake.getHeadX() + ";" + snake.getHeadY());
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

//                gridWidthTextField.setText(String.valueOf(gridDrawer.getGridWidth()));
//                gridHeightTextField.setText(String.valueOf(gridDrawer.getGridHeight()));

                stop = true;
                gameEndWindow.pack();
//                gameEndWindow.setPosition(gridDrawer.getX() + gridDrawer.getWidth() / 2.0f, gridDrawer.getY() + gridDrawer.getHeight() / 2.0f);
//                System.out.println(gameEndWindow.getWidth() + "; " + gameEndWindow.getHeight());
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
            } else gridDrawer.drawCell(mandarinTexture, mandarin.x, mandarin.y, 0);
        }
        snake.draw();
        spriteBatch.end();

        stage.act();
        stage.draw();
    }

    public void drawCell(TextureRegion textureRegion, int x, int y, int rotation) {
//        spriteBatch.draw(
//            textureRegion,
//            x * cellSize, y * cellSize,
//            0, 0,
//            snakeBodyTexture.getRegionWidth(), snakeBodyTexture.getRegionHeight(),
//            1, 1,
//            rotation
//        );
        spriteBatch.draw(
            textureRegion,
            x * cellSize, y * cellSize
//            0, 0,
//            snakeBodyTexture.getRegionWidth(), snakeBodyTexture.getRegionHeight(),
//            1, 1,
//            rotation
        );
    }

//    Array<SnakePart> snakeBody;
//
//    int snakeSpeedRotation;
//    GridVector snakeSpeed;
//    int cameraSpeed = 150;
//
//    @Override
//    public void render(float delta) {
//
//        ScreenUtils.clear(Color.WHITE);
//
//        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
//            camera.translate(0, cameraSpeed*delta);
//        }
//        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
//            camera.translate(0, -cameraSpeed * delta);
//        }
//        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
//            camera.translate(-cameraSpeed * delta, 0);
//        }
//        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
//            camera.translate(cameraSpeed * delta, 0);
//        }
//
//        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
////            snakeSpeed.setAngleDeg(90);
////            snakeSpeedRotation = 90;
//            snakeSpeed.set(0, 1);
//        } else if (Gdx.input.isKeyPressed(Input.Keys.S)) {
////            snakeSpeed.setAngleDeg(270);
////            snakeSpeedRotation = 270;
//            snakeSpeed.set(0, -1);
//        } else if (Gdx.input.isKeyPressed(Input.Keys.A)) {
////            snakeSpeed.setAngleDeg(180);
////            snakeSpeedRotation = 180;
//            snakeSpeed.set(-1, 0);
//        } else if (Gdx.input.isKeyPressed(Input.Keys.D)) {
////            snakeSpeed.setAngleDeg(180);
////            snakeSpeedRotation = 0;
//            snakeSpeed.set(1, 0);
//        }
//
//
//        if (updatePeriod <= System.currentTimeMillis() - lastUpdate) {
//            SnakePart snakePart;
//
//            //Первой после головы части тела змей присваиваются координаты и поворот головы змеи
////            snakeBody.get(1).x = snakePart.x;
////            snakeBody.get(1).y = snakePart.y;
////            snakeBody.get(1).rotation = snakePart.rotation;
//
//            for (int i = 1; i < snakeBody.size; i++) {
//                //Каждая часть тела змеи занимает положение предыдущей
//                snakePart = snakeBody.get(i);
//                snakePart.x = snakeBody.get(i - 1).x;
//                snakePart.y = snakeBody.get(i - 1).y;
//                snakePart.rotation = snakeBody.get(i - 1).rotation;
//            }
//
//            //Голова змеи
//            snakePart = snakeBody.get(0);
//
//            if(snakePart.) {
//                snakePart.rotation = snakeSpeedRotation;
//            }
//
//            //Голова змеи перемещается на одну клетку в направлении, выбранном игроком
////            snakePart.x += (int) MathUtils.cos(snakePart.rotation * MathUtils.degreesToRadians);
////            snakePart.y += (int) MathUtils.sin(snakePart.rotation * MathUtils.degreesToRadians);
//            snakePart.x+=snakeSpeed.x;
//            //И голова змеи рисуется
//
//            for (GridPoint2 mandarin : mandarins) {
//                //Проверяется, съела ли змея какой-нибудь мандарин
//                if (mandarin.x == snakePart.x && mandarin.y == snakePart.y) {
//                    //Если да, то этот мандарин удаляется
//                    mandarins.removeValue(mandarin, true);
//                    //А змее прибавляется часть
//                    snakeBody.add(new SnakePart(0, 0, 0));
//                    //Если нет, то он рисуется
//                }
//            }
//
//            //Хвост змеи занимает положение предыдущей части
////            snakePart = snakeBody.get(snakeBody.size - 1);
////            snakePart.x = snakeBody.get(snakeBody.size - 2).x;
////            snakePart.y = snakeBody.get(snakeBody.size - 2).y;
////            snakePart.rotation = snakeBody.get(snakeBody.size - 2).rotation;
//
//            lastUpdate = System.currentTimeMillis();
//        }
//
//        spriteBatch.begin();
//        camera.update();
//        spriteBatch.setProjectionMatrix(camera.combined);
//
//        //Рисуются клетки
//        for (int i = 0; i < height; i++) {
//            for (int j = 0; j < width; j++) {
//                drawCell(cellTexture, j, i, 0);
//            }
//        }
//
//        //Рисуется голова змеи
//        SnakePart snakePart = snakeBody.get(0);
//
//        drawCell(snakeHeadTexture, snakePart.x, snakePart.y, snakePart.rotation);
//
//        //Рисуется тело змеи
//        for (int i = 1; i < snakeBody.size - 1; i++) {
//            snakePart = snakeBody.get(i);
//            drawCell(snakeBodyTexture, snakePart.x, snakePart.y, snakePart.rotation);
//        }
//
//        //Рисуется хвост змеи
//        snakePart = snakeBody.get(snakeBody.size - 1);
//        drawCell(snakeTailTexture, snakePart.x, snakePart.y, snakePart.rotation);
//
//        //Рисуются мандарины
//        for (GridPoint2 mandarin : mandarins) {
//            drawCell(mandarinTexture, mandarin.x, mandarin.y, 0);
//        }
//
//        spriteBatch.end();
//    }

    @Override
    public void resize(int width, int height) {
//        camera.setToOrtho(false, (float) gridDrawer.getGridWidth() * cellSize * width / height, (float) (gridDrawer.getGridHeight() * cellSize));
        stage.getViewport().update(width, height);
        viewport.getCamera().position.set(gridDrawer.getX() + gridDrawer.getWidth() / 2.0f, gridDrawer.getY() + gridDrawer.getHeight() / 2.0f, 0);
        viewport.getCamera().update();
//         Resize your screen here. The parameters represent the new window size.
    }

    @Override
    public void pause() {
        // Invoked when your application is paused.
    }

    @Override
    public void resume() {
        // Invoked when your application is resumed after pause.
    }

    @Override
    public void hide() {
        // This method is called when another screen replaces this one.
    }

    @Override
    public void dispose() {
        // Destroy screen's assets here.
    }
}
