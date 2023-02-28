package xyz.rajik.field;

import java.util.Arrays;

public class Field {
    private CellType[][] field;


    private int aliveCells;


    private boolean isReadyToBattle = false;

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    private int width;
    private int height;

    public Field(int width, int height) {
        this.width = width;
        this.height = height;
        field = new CellType[height][width];
        for (int i = 0; i < height; i++)
            for (int j = 0; j < width; j++)
                field[i][j] = CellType.EMPTY;
    }

    public void setCell(CellType cellType, int i, int j) {
        field[i][j] = cellType;
    }

    public CellType getCell(int i, int j) {
        return field[i][j];
    }

    public CellType attackCell(int i, int j) {
        CellType cellType = field[i][j];
        if (cellType == CellType.BATTLESHIP) {
            field[i][j] = CellType.DEAD;
            aliveCells--;
        }
        else
            field[i][j] = CellType.CLEARED;
        /*if (cellType == CellType.BATTLESHIP) {
            for (int r = i - 1; r < i + 2; r++)
                for (int c = j - 1; c < j + 2; c++)
                    if (r >= 0 && r < height && c >= 0 && c < width)
                        field[r][c] = CellType.DEAD;
        }*/
        return cellType;
    }

    public CellType[][] getField() {
        return field;
    }

    public void setField(CellType[][] field) {
        this.field = field;
    }

    public boolean isReadyToBattle() {
        return isReadyToBattle;
    }

    public void setReadyToBattle(boolean readyToBattle) {
        isReadyToBattle = readyToBattle;
    }

    public int getAliveCells() {
        return aliveCells;
    }

    public void setAliveCells(int aliveCells) {
        this.aliveCells = aliveCells;
    }

}
