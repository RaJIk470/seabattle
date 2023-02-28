package xyz.rajik;

import xyz.rajik.battleship.Battleship;
import xyz.rajik.field.*;

import javax.swing.*;
import javax.swing.plaf.TableHeaderUI;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class Game {
    public static final Integer GAME_PORT = 8888;
    private Field field;
    private Field enemyField;

    private ServerSocket hostSocket;
    private Socket clientSocket;

    private Boolean isGameOver = false;

    private Boolean isPlayerTurn = false;

    private BattleshipPointReader battleshipPointReader;
    private int opponentCells;

    public Game(int width, int height) {
        field = new Field(width, height);
        enemyField = new Field(width, height);
        battleshipPointReader = new BattleshipPointReader(System.in, width, height);
    }
    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        Game game = new Game(10, 10);
        Scanner in = new Scanner(System.in);
        String type = in.nextLine();
        String[] split = type.toLowerCase().split(" ");

        Thread placingThread = new Thread(() -> {
            int[] battleships = Battleship.getDefaultBattleships();
            BattleshipPlacer battleshipPlacer = new BattleshipPlacer(game.field, battleships);
            game.opponentCells = Arrays.stream(battleships).sum();
            FieldPrinter fieldPrinter = new FieldPrinter();
            while (!game.field.isReadyToBattle()) {
                System.out.println("""
                        Choose option:
                        1. Print current field
                        2. Place ship
                        3. Remove previous ship
                        4. Clear field
                        5. Ready to battle
                        """);
                try {
                    Integer option = Integer.parseInt(in.nextLine());
                    if (option < 1 || option > 5) {
                        System.out.println("Incorrect option");
                        continue;
                    }
                    switch (option) {
                        case 1 -> fieldPrinter.printField(game.field);
                        case 2 ->  {
                            ReaderData readerData = game.battleshipPointReader.readPoint();
                            Point point = readerData.getPoint();
                            battleshipPlacer.placeShip(point.x, point.y, readerData.getVertical());
                            fieldPrinter.printField(game.field);
                        }
                        case 3 -> battleshipPlacer.removeLastShip();
                        case 4 -> battleshipPlacer.clearField();
                        case 5 -> {
                            if (battleshipPlacer.isAllPlaced())
                                game.field.setReadyToBattle(true);
                            else
                                System.out.println("U must place all the ships");
                        }
                    }
                } catch (Exception e) {
                    System.out.println("Incorrect option");
                }
            }
        });

        Thread clientThread = new Thread(() -> {
            try {
                game.connectToGame(split[1]);
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        });

        Thread hostThread = new Thread(() -> {
            try {
                game.hostGame();
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        });

        switch (split[0]) {
            case "connect" -> {
                placingThread.start();
                clientThread.start();
            }

            case "host" -> {
                placingThread.start();
                hostThread.start();
            }
        }
    }

    private void hostGame() throws IOException, InterruptedException, ClassNotFoundException {
        hostSocket = new ServerSocket(GAME_PORT);
        Socket client = hostSocket.accept();
        System.out.println("Client accepted");

        InputStream inputStream = client.getInputStream();
        OutputStream outputStream = client.getOutputStream();

        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
        ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
        Scanner in = new Scanner(inputStream);
        PrintWriter out = new PrintWriter(outputStream);

        Boolean clientIsReady = false;
        while (!clientIsReady) {
            Thread.sleep(1000);
            //System.out.println("Trying to send check command");
            out.println("check");
            out.flush();

            //System.out.println("Trying to retrieve response");
            clientIsReady = Boolean.parseBoolean(in.nextLine());
            //System.out.println("Parsed: " + clientIsReady);
        }

        while (!field.isReadyToBattle()) Thread.sleep(1000);

        out.println("ready");
        out.flush();

        System.out.println("Game has started");
        Boolean isHostTurn = new Random().nextBoolean();
        if (isHostTurn)
            isPlayerTurn = isHostTurn;
        out.println(!isHostTurn);
        out.flush();

        while (!isGameOver) {
            processTurn(objectOutputStream, objectInputStream);
        }

        client.close();
    }
    private Boolean isCorrectPoint(Point point) {
        if (point.x >= field.getHeight() || point.y >= field.getWidth() ||
            point.x < 0 || point.y < 0) return false;

        return true;
    }

    private void processTurn(ObjectOutputStream out, ObjectInputStream in) throws IOException, ClassNotFoundException {
        FieldPrinter fieldPrinter  = new FieldPrinter();
        if (isPlayerTurn) {
            System.out.println("Your turn");
            Scanner userInput = new Scanner(System.in);
            Boolean isCorrectPoint = false;
            Point point = null;
            while (!isCorrectPoint) {
                try {
                    point = new Point(userInput.nextInt(), userInput.nextInt());
                    isCorrectPoint = isCorrectPoint(point);
                    if (!isCorrectPoint)
                        System.out.println("U've entered incorrect point, try again");
                } catch (Exception e) {
                    System.out.println("Invalid input");
                }
            }
            out.writeObject(point);
            CellType cellType = (CellType) in.readObject();
            if (cellType == CellType.BATTLESHIP) {
                enemyField.setCell(CellType.BATTLESHIP, point.x, point.y);
                opponentCells -= 1;
                if (opponentCells == 0) {
                    isGameOver = true;
                    System.out.println("U win");
                    return;
                }
            } else {
                    isPlayerTurn = !isPlayerTurn;
            }
            enemyField.attackCell(point.x, point.y);
        } else {
            System.out.println("Your opponent turn");
            Point point = (Point) in.readObject();
            CellType cellType = field.attackCell(point.x, point.y);
            if (cellType != CellType.BATTLESHIP) isPlayerTurn = !isPlayerTurn;
            out.writeObject(cellType);
            if (field.getAliveCells() == 0) {
                isGameOver = true;
                System.out.println("U lose");
                return;
            }
        }

        System.out.println("\tYour field:");
        fieldPrinter.printField(field);
        System.out.println("\tEnemy field:");
        fieldPrinter.printField(enemyField);
    }

    private void connectToGame(String ip) throws IOException, ClassNotFoundException {
        clientSocket = new Socket(ip, GAME_PORT);
        System.out.println(clientSocket.isConnected());
        System.out.println("Connected to server");

        InputStream inputStream = clientSocket.getInputStream();
        OutputStream outputStream = clientSocket.getOutputStream();

        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
        ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
        Scanner in = new Scanner(inputStream);
        PrintWriter out = new PrintWriter(outputStream);

        while (true) {
            //System.out.println("Getting next command");
            String query = in.nextLine();
            //System.out.println("Read " + query);
            if (query.startsWith("check")) {
                Boolean isReady = field.isReadyToBattle();
                out.println(isReady);
                out.flush();
                //System.out.println("Sended: " + isReady);
            }
            if (query.startsWith("ready")) break;
        }

        System.out.println("Game has started");
        isPlayerTurn = Boolean.parseBoolean(in.nextLine());
        System.out.println(isPlayerTurn);

        while (!isGameOver) {
            processTurn(objectOutputStream, objectInputStream);
        }

        clientSocket.close();
    }

    private void findGames() {

    }


}
