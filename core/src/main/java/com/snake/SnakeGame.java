package com.snake;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.ScreenUtils;

/**
 * {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms.
 */
public class SnakeGame extends Game {
    private Skin skin;
    private SpriteBatch spriteBatch;
    private GameScreen gameScreen;

    private Texture createTexture(String name) {
        if (Gdx.files.isLocalStorageAvailable() && Gdx.files.local("textures/" + name + ".png").exists()) {
            return new Texture(Gdx.files.local("textures/" + name + ".png"));
        } else {
            return new Texture(Gdx.files.internal(name + ".png"));
        }
    }

    @Override
    public void create() {
        spriteBatch = new SpriteBatch();

        skin = new Skin(Gdx.files.internal("vis-skin/uiskin.json"));

        skin.add("cell", createTexture("cell"));
        skin.add("mandarin", createTexture("mandarin"));
        skin.add("snakeHead", createTexture("snakeHead"));
        skin.add("snakeBody", createTexture("snakeBody"));
        skin.add("snakeTail", createTexture("snakeTail"));

        skin.add("cell", new Texture(Gdx.files.internal("cell.png")));
        skin.add("mandarin", new Texture(Gdx.files.internal("mandarin.png")));
        skin.add("snakeHead", new Texture(Gdx.files.internal("snakeHead.png")));
        skin.add("snakeBody", new Texture(Gdx.files.internal("snakeBody.png")));
        skin.add("snakeTail", new Texture(Gdx.files.internal("snakeTail.png")));

        gameScreen = new GameScreen(this);
        setScreen(gameScreen);
    }

    @Override
    public void render() {
        super.render();
//        ScreenUtils.clear(Color.WHITE);
//        gameScreen.render(Gdx.graphics.getDeltaTime());
    }

    public Skin getSkin() {
        return skin;
    }

    public SpriteBatch getSpriteBatch() {
        return spriteBatch;
    }

    @Override
    public void dispose() {
        super.dispose();
        gameScreen.dispose();
        skin.dispose();
        spriteBatch.dispose();
    }
}
