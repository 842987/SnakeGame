package com.snake;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class GridDrawer {

    private TextureRegion cellTexture;
    private Batch batch;

    private float cellWidth;
    private float cellHeight;

    private int gridWidth;
    private int gridHeight;

    private float width;
    private float height;

    private float x;
    private float y;

    public GridDrawer(TextureRegion cellTexture, int gridWidth, int gridHeight, int cellWidth, int cellHeight) {
        this.cellTexture = cellTexture;

        setCellSize(cellWidth, cellHeight);

        width = gridWidth * cellWidth;
        height = gridHeight * cellHeight;

        setGridSize(gridWidth, gridHeight);

        this.x = 0;
        this.y = 0;
    }

    private void drawCells() {
        for (int i = 0; i <= gridHeight; i++) {
            for (int j = 0; j <= gridWidth; j++) {
                drawCell(cellTexture, j, i, 0);
            }
        }
    }

    public void setGridSize(int width, int height) {
        this.gridWidth = width;
        this.gridHeight = height;
        setSize(this.width, this.height);
    }

    public int getGridWidth() {
        return gridWidth;
    }

    public int getGridHeight() {
        return gridHeight;
    }

    public void setSize(float width, float height) {
        this.width = width;
        this.height = height;
        cellWidth *= width / (gridWidth * cellWidth);
        cellHeight *= height / (gridHeight * cellHeight);
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public void setCellSize(float width, float height) {
        this.cellWidth = width;
        this.cellHeight = height;
    }

    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public void begin(Batch batch) {
        this.batch = batch;
        drawCells();
    }

    public void drawCell(TextureRegion texture, int gridX, int gridY, int rotation) {
        batch.draw(
            texture,
            gridX * cellWidth + this.x, gridY * cellHeight + this.y,
            cellWidth / 2.0f, cellHeight / 2.0f,
            cellWidth, cellHeight,
            1.0f, 1.0f,
            rotation
        );
    }

//    public void drawCell(TextureRegion texture, int x, int y) {
//        if(!spriteBatch.isDrawing()) {
//            spriteBatch.begin();
//            drawCells();
//        }
//        batch.draw(texture, x * cellWidth, y * cellHeight);
//    }
}
