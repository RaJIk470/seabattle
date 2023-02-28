package xyz.rajik.field;

import xyz.rajik.Point;
import xyz.rajik.battleship.Battleship;
import xyz.rajik.exception.IncorrectPointException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class BattleshipPlacer {
    private Field field;
    private int[] battleships;
    private int currentShip = 0;

    public boolean isAllPlaced() {
        return isAllPlaced;
    }

    public void setAllPlaced(boolean allPlaced) {
        isAllPlaced = allPlaced;
    }

    private boolean isAllPlaced = false;

    private List<Battleship> battleshipPoints;

    public BattleshipPlacer(Field field, int[] battleships) {
        this.battleships = battleships;
        this.field = field;
        field.setAliveCells(Arrays.stream(battleships).sum());
        battleshipPoints = new ArrayList<>();
    }

    private boolean isCorrectCoordinate(int i, int j) {
        return i >= 0 && j >= 0 && i < field.getHeight() && j < field.getHeight();
    }

    private boolean hasShipsNear(int i, int j) {
        for (int r = i - 1; r < i + 2; r++) {
            for (int c = j - 1; c < j + 2; c++) {
                if (r >= 0 && r < field.getHeight() && c >= 0 && c < field.getWidth()) {
                    if (field.getCell(r, c) == CellType.BATTLESHIP)
                        return true;
                }
            }
        }

        return false;
    }

    public boolean placeShip(int i, int j, boolean isVertical) {
        if (!(isCorrectCoordinate(i, j) && !isAllPlaced)) throw new IncorrectPointException();
        List<Point> points = new ArrayList<>(4);

        int shipLength = battleships[currentShip];
        if (isVertical) {
            if (i + shipLength - 1 >= field.getWidth()) throw new IncorrectPointException();
            for (int k = i; k < i + shipLength; k++) {
                if (hasShipsNear(k, j)) throw new IncorrectPointException();
                points.add(new Point(k, j));
            }
        } else {
            if (j + shipLength - 1 >= field.getHeight()) throw new IncorrectPointException();
            for (int k = j; k < j + shipLength; k++) {
                if (hasShipsNear(i, k)) throw new IncorrectPointException();
                points.add(new Point(i, k));
            }
        }

        Battleship battleship = new Battleship(points);
        battleshipPoints.add(battleship);

        for (var point : points)
            field.setCell(CellType.BATTLESHIP, point.x, point.y);

        currentShip++;

        if (currentShip == battleships.length)  {
            //field.setReadyToBattle(true);
            isAllPlaced = true;
        }

        return isAllPlaced;
    }

    public void removeLastShip() {
        Battleship battleship = battleshipPoints.remove(battleshipPoints.size() - 1);
        for (Point point : battleship.getShipPoints())
            field.setCell(CellType.EMPTY, point.x, point.y);
        currentShip--;
        isAllPlaced = false;
    }

    public void clearField() {
        while (battleshipPoints.size() > 0) removeLastShip();
    }
}
