package xyz.rajik.battleship;

import xyz.rajik.Point;

import java.util.List;

public class Battleship {
    private List<Point> shipPoints;

    public Battleship(List<Point> points) {
        this.shipPoints = points;
    }
    public static int[] getDefaultBattleships() {
        return new int[]{4, 3, 3, 2, 2, 2, 1, 1, 1, 1};
    }

    public static int[] getReducedBattleships() {
        return new int[]{2, 1};
    }


    public List<Point> getShipPoints() {
        return shipPoints;
    }

    public void setShipPoints(List<Point> shipPoints) {
        this.shipPoints = shipPoints;
    }
}
