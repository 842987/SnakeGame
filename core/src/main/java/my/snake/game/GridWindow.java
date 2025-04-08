package my.snake.game;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Window;

public class GridWindow extends Window {
    Grid grid;
    public GridWindow(String title, Skin skin, WindowStyle style) {
        super(title, style);

//        grid = new Grid(skin);
        grid.setPosition(0.0f, 0.0f);
        add(grid);
//        setSize();
    }
    public GridWindow(String title, Skin skin) {
        super(title, skin);
    }

    @Override
    public void layout() {
        super.layout();
//        grid.setGridSize(getWidth(), getHeight());
        grid.setSize(getWidth(), getHeight());
//        System.out.println("layout");
    }

//    @Override
//    protected void sizeChanged() {
//        super.sizeChanged();
//        grid.setSize(getWidth(), getHeight());
//        System.out.println("Size changed");
//    }
}
