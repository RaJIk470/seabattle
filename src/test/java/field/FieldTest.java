package field;

import org.junit.jupiter.api.Test;
import xyz.rajik.battleship.Battleship;
import xyz.rajik.field.BattleshipPlacer;
import xyz.rajik.field.CellType;
import xyz.rajik.field.Field;
import xyz.rajik.field.FieldPrinter;

public class FieldTest {
    private static FieldPrinter fieldPrinter = new FieldPrinter();
    @Test
    public void printTest() {
        Field field = new Field(10, 10);
        fieldPrinter.printField(field);
    }

    @Test
    public void attackTest() {
        Field field = new Field(10, 10);
        field.setCell(CellType.BATTLESHIP, 2, 5);
        field.attackCell(2, 5);

        field.setCell(CellType.BATTLESHIP, 0, 0);
        field.attackCell(0, 0);

        field.setCell(CellType.BATTLESHIP, 9, 9);
        field.attackCell(9, 9);
        fieldPrinter.printField(field);
    }

    @Test
    public void simplePlaceTest() {
        Field field = new Field(10, 10);
        BattleshipPlacer placer = new BattleshipPlacer(field, Battleship.getDefaultBattleships());

        placer.placeShip(3, 5, true);
        placer.placeShip(2, 1, true);
        placer.placeShip(0, 4, false);

        fieldPrinter.printField(field);
    }
}
