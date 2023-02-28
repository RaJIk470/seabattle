package xyz.rajik.field;

public enum CellType {
    EMPTY('.'),
    BATTLESHIP('O'),
    DEAD('x'),
    CLEARED('*');
    private Character name;


    CellType(Character name) {
        this.name = name;
    }

    public Character getName() {
        return name;
    }

}
