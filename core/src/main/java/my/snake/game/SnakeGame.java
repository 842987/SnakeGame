package my.snake.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

/**
 * {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms.
 */
public class SnakeGame extends Game {

    private Skin skin;
    private SpriteBatch spriteBatch;

    @Override
    public void create() {
        skin = new Skin(Gdx.files.internal("skin/metal-ui.json"));
        spriteBatch = new SpriteBatch();

        skin.add("cell", new Texture(Gdx.files.internal("cell.png")));
        skin.add("mandarin", new Texture(Gdx.files.internal("mandarin.png")));
        skin.add("snakeHead", new Texture(Gdx.files.internal("snakeHead3.png")));
        skin.add("snakeBody", new Texture(Gdx.files.internal("snakeBody.png")));
        skin.add("snakeTail", new Texture(Gdx.files.internal("snakeTail.png")));

//        skin.load(Gdx.files.internal("skin/metal-ui.json"));

        setScreen(new GameScreen(this));
    }

    public Skin getSkin() {
        return skin;
    }

    public SpriteBatch getSpriteBatch() {
        return spriteBatch;
    }
}
