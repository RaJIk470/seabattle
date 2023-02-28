package xyz.rajik.field;

import xyz.rajik.Point;

public class ReaderData {
    private Point point;
    private Boolean isVertical;

    public ReaderData(Point point, Boolean isVertical) {
        this.point = point;
        this.isVertical = isVertical;
    }

    public Point getPoint() {
        return point;
    }

    public void setPoint(Point point) {
        this.point = point;
    }

    public Boolean getVertical() {
        return isVertical;
    }

    public void setVertical(Boolean vertical) {
        isVertical = vertical;
    }
}
