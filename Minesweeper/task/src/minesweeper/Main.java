package minesweeper;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        new Minesweeper().run();
    }
}

class Minesweeper implements Runnable {

    private final Scanner sc = new Scanner(System.in);
    private final char[][] hiddenField = new char[9][9]; //contains all mines and safe cells
    private char[][] playersField = new char[9][9]; //contains unexplored field, changes while playing
    private final char MINE = 'X';
    private final char SAFE = '/';
    private final char UNEXPLORED = '.';
    private final char MARKED = '*';
    private final List<Character> NUMBERS = Arrays.asList('1', '2', '3', '4', '5', '6', '7', '8');
    private ArrayList<String> minesList;
    private final ArrayList<String> markedCellsList = new ArrayList<>();
    private final ArrayList<String> unexploredCellsList = new ArrayList<>();

    private void fillFieldWithMines(int mines) {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                hiddenField[i][j] = UNEXPLORED;
            }
        }

        Random r = new Random();
        minesList = new ArrayList<>();

        for (int i = 0; i < mines; i++) {
            int r1 = r.nextInt(9);
            int r2 = r.nextInt(9);
            if (hiddenField[r1][r2] == UNEXPLORED) {
                hiddenField[r1][r2] = MINE;
                minesList.add(r1 + "" + r2);
            } else {
                i--;
            }
        }
        Collections.sort(minesList);
    }

    private void fillFieldWithNumbers() {
        int n = hiddenField.length - 1;
        int m = hiddenField[0].length - 1;
        for (int i = 0; i < hiddenField.length; i++) {
            for (int j = 0; j < hiddenField[0].length; j++) {
                if (hiddenField[i][j] == MINE) {
                    continue;
                }
                int mines = 0;
                if (i != 0 && j != 0 && i != m && j != m) {
                    mines += hiddenField[i - 1][j - 1] == MINE ? 1 : 0;
                    mines += hiddenField[i - 1][j] == MINE ? 1 : 0;
                    mines += hiddenField[i - 1][j + 1] == MINE ? 1 : 0;
                    mines += hiddenField[i][j - 1] == MINE ? 1 : 0;
                    mines += hiddenField[i][j + 1] == MINE ? 1 : 0;
                    mines += hiddenField[i + 1][j - 1] == MINE ? 1 : 0;
                    mines += hiddenField[i + 1][j] == MINE ? 1 : 0;
                    mines += hiddenField[i + 1][j + 1] == MINE ? 1 : 0;
                } else if (i == 0 && j == 0) {
                    mines += hiddenField[i][j + 1] == MINE ? 1 : 0;
                    mines += hiddenField[i + 1][j] == MINE ? 1 : 0;
                    mines += hiddenField[i + 1][j + 1] == MINE ? 1 : 0;
                } else if (i == 0 && j == m) {
                    mines += hiddenField[i][j - 1] == MINE ? 1 : 0;
                    mines += hiddenField[i + 1][j - 1] == MINE ? 1 : 0;
                    mines += hiddenField[i + 1][j] == MINE ? 1 : 0;
                } else if (i == 0) {
                    mines += hiddenField[i][j - 1] == MINE ? 1 : 0;
                    mines += hiddenField[i][j + 1] == MINE ? 1 : 0;
                    mines += hiddenField[i + 1][j - 1] == MINE ? 1 : 0;
                    mines += hiddenField[i + 1][j] == MINE ? 1 : 0;
                    mines += hiddenField[i + 1][j + 1] == MINE ? 1 : 0;
                } else if (i == n && j == 0) {
                    mines += hiddenField[i - 1][j] == MINE ? 1 : 0;
                    mines += hiddenField[i - 1][j + 1] == MINE ? 1 : 0;
                    mines += hiddenField[i][j + 1] == MINE ? 1 : 0;
                } else if (i == n && j == m) {
                    mines += hiddenField[i - 1][j - 1] == MINE ? 1 : 0;
                    mines += hiddenField[i - 1][j] == MINE ? 1 : 0;
                    mines += hiddenField[i][j - 1] == MINE ? 1 : 0;
                } else if (i == n) {
                    mines += hiddenField[i - 1][j - 1] == MINE ? 1 : 0;
                    mines += hiddenField[i - 1][j] == MINE ? 1 : 0;
                    mines += hiddenField[i - 1][j + 1] == MINE ? 1 : 0;
                    mines += hiddenField[i][j - 1] == MINE ? 1 : 0;
                    mines += hiddenField[i][j + 1] == MINE ? 1 : 0;
                } else if (j == 0) {
                    mines += hiddenField[i - 1][j] == MINE ? 1 : 0;
                    mines += hiddenField[i - 1][j + 1] == MINE ? 1 : 0;
                    mines += hiddenField[i][j + 1] == MINE ? 1 : 0;
                    mines += hiddenField[i + 1][j] == MINE ? 1 : 0;
                    mines += hiddenField[i + 1][j + 1] == MINE ? 1 : 0;
                } else if (j == m) {
                    mines += hiddenField[i - 1][j - 1] == MINE ? 1 : 0;
                    mines += hiddenField[i - 1][j] == MINE ? 1 : 0;
                    mines += hiddenField[i][j - 1] == MINE ? 1 : 0;
                    mines += hiddenField[i + 1][j - 1] == MINE ? 1 : 0;
                    mines += hiddenField[i + 1][j] == MINE ? 1 : 0;
                }
                hiddenField[i][j] = mines != 0 ? (char) (mines + '0') : SAFE;
            }
        }
    }

    private void fillPlayersField() {
        playersField = new char[hiddenField.length][hiddenField[0].length];
        for (int i = 0; i < hiddenField.length; i++) {
            for (int j = 0; j < hiddenField[0].length; j++) {
                playersField[i][j] = UNEXPLORED;
                unexploredCellsList.add(i + "" + j);
            }
        }
    }

    private void displayPlayersField() {
        System.out.println(" |123456789|");
        System.out.println("—|—————————|");
        for (int i = 0; i < hiddenField.length; i++) {
            for (int j = 0; j < hiddenField[0].length; j++) {
                //border
                if (j == 0) {
                    System.out.print(i + 1 + "|");
                }

                //field
                System.out.print(playersField[i][j]);

                //border
                if (j == hiddenField[0].length - 1) {
                    System.out.print("|");
                }
            }
            System.out.println();
        }
        System.out.println("—|—————————|");
    }

    private void displayMines() {
        for (int i = 0; i < playersField.length; i++) {
            for (int j = 0; j < playersField[0].length; j++) {
                if (hiddenField[i][j] == MINE) {
                    playersField[i][j] = MINE;
                }
            }
        }
    }

    private void exploreAllSafeCellsAround(ArrayList<String> cells) {
        for (String cell : cells) {
            int bufX = Integer.parseInt(cell.substring(0, 1));
            int bufY = Integer.parseInt(cell.substring(1));
            playersField[bufX][bufY] = hiddenField[bufX][bufY];
            unexploredCellsList.remove(bufX + "" + bufY);
            if (!getSafeCellsAround(bufX, bufY).isEmpty() || !getNumbersAround(bufX, bufY).isEmpty()) {
                exploreAllNumbersAround(getNumbersAround(bufX, bufY));
                exploreAllSafeCellsAround(getSafeCellsAround(bufX, bufY));
            }
        }
    }

    private void exploreAllNumbersAround(ArrayList<String> cells) {
        for (String cell : cells) {
            int bufX = Integer.parseInt(cell.substring(0, 1));
            int bufY = Integer.parseInt(cell.substring(1));
            playersField[bufX][bufY] = hiddenField[bufX][bufY];
        }
    }

    private boolean isNumber(char c) {
        return NUMBERS.contains(c);
    }

    private ArrayList<String> getSafeCellsAround(int x, int y) {
        ArrayList<String> cells = new ArrayList<>();
        int bufX;
        int bufY;

        //left
        if (y != 0) {
            bufX = x;
            bufY = y - 1;
            if (hiddenField[bufX][bufY] == SAFE && (playersField[bufX][bufY] == UNEXPLORED || playersField[bufX][bufY] == MARKED)) {
                cells.add(bufX + "" + bufY);
            }

            if (x != 0) {
                bufX = x - 1;
                if (hiddenField[bufX][bufY] == SAFE && (playersField[bufX][bufY] == UNEXPLORED || playersField[bufX][bufY] == MARKED)) {
                    cells.add(bufX + "" + bufY);
                }
            }

            if (x != 8) {
                bufX = x + 1;
                if (hiddenField[bufX][bufY] == SAFE && (playersField[bufX][bufY] == UNEXPLORED || playersField[bufX][bufY] == MARKED)) {
                    cells.add(bufX + "" + bufY);
                }
            }
        }

        //centre
        if (x != 0) {
            bufX = x - 1;
            bufY = y;
            if (hiddenField[bufX][bufY] == SAFE && (playersField[bufX][bufY] == UNEXPLORED || playersField[bufX][bufY] == MARKED)) {
                cells.add(bufX + "" + bufY);
            }
        }

        if (x != 8) {
            bufX = x + 1;
            bufY = y;
            if (hiddenField[bufX][bufY] == SAFE && (playersField[bufX][bufY] == UNEXPLORED || playersField[bufX][bufY] == MARKED)) {
                cells.add(bufX + "" + bufY);
            }
        }

        //right
        if (y != 8) {
            bufX = x;
            bufY = y + 1;
            if (hiddenField[bufX][bufY] == SAFE && (playersField[bufX][bufY] == UNEXPLORED || playersField[bufX][bufY] == MARKED)) {
                cells.add(bufX + "" + bufY);
            }

            if (x != 0) {
                bufX = x - 1;
                if (hiddenField[bufX][bufY] == SAFE && (playersField[bufX][bufY] == UNEXPLORED || playersField[bufX][bufY] == MARKED)) {
                    cells.add(bufX + "" + bufY);
                }
            }

            if (x != 8) {
                bufX = x + 1;
                if (hiddenField[bufX][bufY] == SAFE && (playersField[bufX][bufY] == UNEXPLORED || playersField[bufX][bufY] == MARKED)) {
                    cells.add(bufX + "" + bufY);
                }
            }
        }
        return cells;
    }

    private ArrayList<String> getNumbersAround(int x, int y) {
        ArrayList<String> cells = new ArrayList<>();
        int bufX;
        int bufY;

        //left
        if (y != 0) {
            bufX = x;
            bufY = y - 1;
            if (isNumber(hiddenField[bufX][bufY]) && (playersField[bufX][bufY] == UNEXPLORED || playersField[bufX][bufY] == MARKED)) {
                cells.add(bufX + "" + bufY);
            }

            if (x != 0) {
                bufX = x - 1;
                if (isNumber(hiddenField[bufX][bufY]) && (playersField[bufX][bufY] == UNEXPLORED || playersField[bufX][bufY] == MARKED)) {
                    cells.add(bufX + "" + bufY);
                }
            }

            if (x != 8) {
                bufX = x + 1;
                if (isNumber(hiddenField[bufX][bufY]) && (playersField[bufX][bufY] == UNEXPLORED || playersField[bufX][bufY] == MARKED)) {
                    cells.add(bufX + "" + bufY);
                }
            }
        }

        //centre
        if (x != 0) {
            bufX = x - 1;
            bufY = y;
            if (isNumber(hiddenField[bufX][bufY]) && (playersField[bufX][bufY] == UNEXPLORED || playersField[bufX][bufY] == MARKED)) {
                cells.add(bufX + "" + bufY);
            }
        }

        if (x != 8) {
            bufX = x + 1;
            bufY = y;
            if (isNumber(hiddenField[bufX][bufY]) && (playersField[bufX][bufY] == UNEXPLORED || playersField[bufX][bufY] == MARKED)) {
                cells.add(bufX + "" + bufY);
            }
        }

        //right
        if (y != 8) {
            bufX = x;
            bufY = y + 1;
            if (isNumber(hiddenField[bufX][bufY]) && (playersField[bufX][bufY] == UNEXPLORED || playersField[bufX][bufY] == MARKED)) {
                cells.add(bufX + "" + bufY);
            }

            if (x != 0) {
                bufX = x - 1;
                if (isNumber(hiddenField[bufX][bufY]) && (playersField[bufX][bufY] == UNEXPLORED || playersField[bufX][bufY] == MARKED)) {
                    cells.add(bufX + "" + bufY);
                }
            }

            if (x != 8) {
                bufX = x + 1;
                if (isNumber(hiddenField[bufX][bufY]) && (playersField[bufX][bufY] == UNEXPLORED || playersField[bufX][bufY] == MARKED)) {
                    cells.add(bufX + "" + bufY);
                }
            }
        }
        return cells;
    }

    @Override
    public void run() {
        System.out.print("How many mines do you want on the field? ");
        int mines = sc.nextInt();
        System.out.println();

        fillFieldWithMines(mines);
        fillFieldWithNumbers();
        fillPlayersField();
        displayPlayersField();

        String choice = "";
        while (!choice.equals("free")) { //first exploring cannot be a mine
            System.out.print("Set/unset mine marks or claim a cell as free: ");
            int y = sc.nextInt() - 1;
            int x = sc.nextInt() - 1;
            choice = sc.next().trim().toLowerCase();
            if (choice.equals("free")) {
                while (hiddenField[x][y] == MINE) {
                    fillFieldWithMines(mines);
                    fillFieldWithNumbers();
                }
                playersField[x][y] = hiddenField[x][y];
                unexploredCellsList.remove(x + "" + y);
                if (playersField[x][y] == SAFE){
                    exploreAllNumbersAround(getNumbersAround(x, y));
                    exploreAllSafeCellsAround(getSafeCellsAround(x, y));
                }
                Collections.sort(unexploredCellsList);
            } else if (choice.equals("mine")){
                if (playersField[x][y] == UNEXPLORED) {
                    playersField[x][y] = MARKED;
                } else if (playersField[x][y] == MARKED) {
                    playersField[x][y] = UNEXPLORED;
                }
            }
            displayPlayersField();
        }

        //if player explored first cell
        boolean fail = false;
        while (!fail && !minesList.equals(markedCellsList) && !minesList.equals(unexploredCellsList)) {
            System.out.print("Set/unset mine marks or claim a cell as free: ");
            int y = sc.nextInt() - 1;
            int x = sc.nextInt() - 1;
            choice = sc.next().trim().toLowerCase();
            switch (choice) {
                case "mine":
                    if (playersField[x][y] == UNEXPLORED) {
                        playersField[x][y] = MARKED;
                        markedCellsList.add(x + "" + y);
                    } else if (playersField[x][y] == MARKED){
                        playersField[x][y] = UNEXPLORED;
                        markedCellsList.remove(x + "" + y);
                    }
                    Collections.sort(markedCellsList);
                    break;
                case "free":
                    if (hiddenField[x][y] == MINE) {
                        displayMines();
                        fail = true;
                    } else if (isNumber(playersField[x][y])) {
                        System.out.println("This is a number");
                    } else if (playersField[x][y] == SAFE) {
                        System.out.println("This field is save");
                    } else if (isNumber(hiddenField[x][y])){
                        playersField[x][y] = hiddenField[x][y];
                        unexploredCellsList.remove(x + "" + y);
                        Collections.sort(unexploredCellsList);
                    } else {
                        exploreAllNumbersAround(getNumbersAround(x, y));
                        exploreAllSafeCellsAround(getSafeCellsAround(x, y));
                        Collections.sort(unexploredCellsList);
                    }
                    break;
                default:
                    System.out.println("Unsupported statement! Try use 'mine' or 'free'");
            }
            displayPlayersField();
        }

        if (fail) {
            System.out.println("You stepped on a mine and failed!");
        } else {
            System.out.println("Congratulations! You found all the mines!");
        }
    }
}