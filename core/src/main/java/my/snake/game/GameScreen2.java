package my.snake.game;

import com.badlogic.gdx.Audio;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.AudioRecorder;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.GeometryUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.*;

import javax.sound.sampled.*;
import java.awt.*;

public class GameScreen2 implements Screen {

    Stage stage;

    Batch batch;
    //    Viewport gridViewport;
    Grid grid;

    public GameScreen2(SnakeGame snakeGame) {
        batch = snakeGame.getSpriteBatch();

        stage = new Stage(new ExtendViewport(500, 500), batch);
        Gdx.input.setInputProcessor(stage);

        Skin skin = snakeGame.getSkin();

        Window table = new Window("Test", new Window.WindowStyle(new BitmapFont(), Color.BLACK, skin.getDrawable("cell")));

//        table.add(new TextButton("Test", new TextButton.TextButtonStyle(skin.getDrawable("cell"), null, null, new BitmapFont())));
//        table.add(new Grid(skin));

//        GridWindow gridWindow = new GridWindow("Test", skin, new Window.WindowStyle(new BitmapFont(), Color.BLACK, skin.getDrawable("cell")));
//        gridWindow.pad(15.0f);
//        gridWindow.setSize(500, 500);
//        gridWindow.setModal(true);
//        gridWindow.setMovable(true);
//        stage.addActor(gridWindow);
//
//        gridViewport = new ExtendViewport(150, 100);

        table.setPosition(0, 0);
        table.add(new Button(new Button.ButtonStyle(skin.getDrawable("cell"), skin.getDrawable("mandarin"), null))).grow();
        stage.addActor(table);

        grid = new Grid(skin, 5, 5, 3, 3, 180);
        grid.setSize(250, 250);
//        System.out.println(grid.getWidth());
        grid.setPosition(0, 0);
        stage.addActor(grid);

//        grid.reset(10, 10, 3, 3, 180);
//        grid.setSize(500, 500);
//        grid.setSize(Gdx.graphics.getHeight(), Gdx.graphics.getHeight());

    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.WHITE);
//        stage.getViewport().apply(true);
        stage.act();
        stage.draw();
//        batch.begin();
//        grid.draw(batch, 1);
//        batch.end();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
//        gridViewport.update(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
