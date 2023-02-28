package xyz.rajik.field;

import xyz.rajik.Point;
import xyz.rajik.exception.IncorrectPointException;

import java.awt.*;
import java.io.InputStream;
import java.util.Scanner;

public class BattleshipPointReader {
    private Scanner scanner;
    private int width;
    private int height;

    public BattleshipPointReader(InputStream in, int width, int height) {
        scanner = new Scanner(in);
        this.width = width;
        this.height = height;
    }

    public ReaderData readPoint() {
        System.out.println("Enter a point in this format: char number v/h");
        Character row = Character.toLowerCase(scanner.next().charAt(0));
        Integer col = scanner.nextInt();
        Character vertical = Character.toLowerCase(scanner.next().charAt(0));
        if (vertical != 'v' && vertical != 'h') throw new IncorrectPointException();
        Boolean isVertical = vertical == 'v' ? true : false;
        int rowIndex = row - 'a';
        if (rowIndex < 0 || rowIndex >= height || col < 0 || col >= width) throw new IncorrectPointException();

        return new ReaderData(new Point(rowIndex, col), isVertical);
    }
}
