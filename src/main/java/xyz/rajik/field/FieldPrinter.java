package xyz.rajik.field;

public class FieldPrinter {
    public void printField(Field field) {
        int width = field.getWidth();
        int height = field.getHeight();
        System.out.println("  0 1 2 3 4 5 6 7 8 9");
        for (int i = 0; i < height; i++) {
            System.out.print((char)('A' + i) + " ");
            for (int j = 0; j < width; j++) {
                System.out.print(field.getCell(i, j).getName() + " ");
            }
            System.out.println();
        }
    }

    public void printFieldWithoutShips(Field field) {
        int width = field.getWidth();
        int height = field.getHeight();
        System.out.println("  0 1 2 3 4 5 6 7 8 9");
        for (int i = 0; i < height; i++) {
            System.out.print((char)('A' + i) + " ");
            for (int j = 0; j < width; j++) {
                if (field.getCell(i, j) == CellType.BATTLESHIP)
                    System.out.print(CellType.EMPTY.getName() + " ");
                else
                    System.out.print(field.getCell(i, j).getName() + " ");
            }
            System.out.println();
        }
    }
}
