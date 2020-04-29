package ca.cmpt213.as4.restapi.models;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

/*
 * MazeGame represents the core game logic for this project.
 * It initiates maze and applies techniques to check for a qualified maze
 * MazeGame handles all entities' movement and updates game state.
 * It also provides public interface for UI to interact with.
 */
public class MazeGame {
    private static final int DEFAULT_CHEESE_TO_WIN = 5;
    private static final char DEFAULT_CAT_SYMBOL = '!';
    private static final char DEFAULT_MOUSE_SYMBOL = '@';
    private static final char DEFAULT_CHEESE_SYMBOL = '$';
    private static final char DEFAULT_MOUSE_EATEN_BY_CAT_SYMBOL = 'X';
    private static final char DEFAULT_WALL_SYMBOL = '#';
    private static final char DEFAULT_OPEN_SYMBOL = ' ';
    private static final char DEFAULT_UNEXPLORED_SYMBOL = '.';

    private boolean isMazeRevealed = false;
    private char catSymbol, mouseSymbol, cheeseSymbol;
    private int cheeseCollected = 0;
    private int cheeseToWin;
    private boolean[][] explored;
    private GameState state = GameState.RUNNING;
    private EntityPosition cat1, cat2, cat3, mouse, cheese;
    private Maze maze;
    public MyMazeGenerator generator = new MyMazeGenerator();

    public MazeGame(int row, int col) {
        //Construct qualified maze
        do {
            maze = new Maze(row, col, generator);
        } while (!checkConstraint());

        //Initialize game entities
        mouse = new EntityPosition(1, 1);
        cat1 = new EntityPosition(1, col - 2);
        cat2 = new EntityPosition(row - 2, col - 2);
        cat3 = new EntityPosition(row - 2, 1);

        generateCheese();

        //Initialize symbols for entities
        cheeseToWin = DEFAULT_CHEESE_TO_WIN;
        catSymbol = DEFAULT_CAT_SYMBOL;
        mouseSymbol = DEFAULT_MOUSE_SYMBOL;
        cheeseSymbol = DEFAULT_CHEESE_SYMBOL;

        //Initialize explored map
        explored = new boolean[row][col];
        for (int i = 0; i < row; ++i) {
            for (int j = 0; j < col; ++j) {
                if (i == 0 || j == 0 || i == row - 1 || j == col - 1) { //Boundary walls should be explored and displayed to the player
                    explored[i][j] = true;
                } else {
                    explored[i][j] = false;
                }
            }
        }
        exploreMaze();
    }

    public MazeGame(int row, int col, char catSymbol, char mouseSymbol, char cheeseSymbol) {
        this(row, col);
        this.catSymbol = catSymbol;
        this.mouseSymbol = mouseSymbol;
        this.cheeseSymbol = cheeseSymbol;
    }

    public int getMazeHeight() {
        return maze.getRow();
    }

    public int getMazeWidth() {
        return maze.getCol();
    }

    public EntityPosition getMousePos() {
        return new EntityPosition(mouse.getYCoordinate(), mouse.getXCoordinate());
    }

    public EntityPosition getCheesePos() {
        return new EntityPosition(cheese.getYCoordinate(), cheese.getXCoordinate());
    }

    public ArrayList<EntityPosition> getCatsPos() {
        ArrayList<EntityPosition> catsPos = new ArrayList<>();
        catsPos.add(new EntityPosition(cat1.getYCoordinate(), cat1.getXCoordinate()));
        catsPos.add(new EntityPosition(cat2.getYCoordinate(), cat2.getXCoordinate()));
        catsPos.add(new EntityPosition(cat3.getYCoordinate(), cat3.getXCoordinate()));
        return catsPos;
    }

    public boolean[][] getBooleanMaze() {
        return maze.getBooleanMaze();
    }

    public boolean[][] getExplored() {
        return explored;
    }

    public void setMazeRevealed(boolean mazeRevealed) {
        isMazeRevealed = mazeRevealed;
        for (int i = 0; i < maze.getRow(); ++i) {
            for (int j = 0; j < maze.getCol(); ++j) {
                explored[i][j] = true;
            }
        }
    }

    public void setCheeseToWin(int num) {
        cheeseToWin = num;
    }

    public int getCheeseToWin() {
        return cheeseToWin;
    }

    public int getCheeseCollected() {
        return cheeseCollected;
    }

    public GameState getState() {
        return state;
    }

    public String getMazeAsString() {
        if (!this.isMazeRevealed) {
            return incompleteMazeToString();
        } else {
            return completeMazeToString();
        }
    }

    public void run(MoveDirection userMove) {
        if (userMove == MoveDirection.UNKNOWN) {
            return;
        }
//        this.isMazeRevealed = false;
        moveEntity(mouse, userMove);
        updateState();
        if (state == GameState.LOSING || state == GameState.WINNING) {
            return;
        }
        if (state == GameState.CHEESE_COLLECTING) {
            generateCheese();
            state = GameState.RUNNING;
        }
        //moveCats();
        updateState();
        exploreMaze();

    }


    private boolean fourBlocksConstraintSatisfied(int i, int j) {
        boolean are4BlocksWall = maze.isWall(i, j) && maze.isWall(i, j + 1) && maze.isWall(i + 1, j) && maze.isWall(i + 1, j + 1);
        boolean are4BlocksOpen = !maze.isWall(i, j) && !maze.isWall(i, j + 1) && !maze.isWall(i + 1, j) && !maze.isWall(i + 1, j + 1);
        return !are4BlocksWall && !are4BlocksOpen;
    }

    private boolean hasPathToAllOpenCells() {
        boolean[][] isVisited = new boolean[maze.getRow()][maze.getCol()];
        Queue<EntityPosition> openCells = new LinkedList<>();
        openCells.add(new EntityPosition(1,1));
        while (!openCells.isEmpty()) {
            EntityPosition curPos = openCells.remove();
            int curRow = curPos.getYCoordinate();
            int curCol = curPos.getXCoordinate();
            isVisited[curRow][curCol] = true;
            for (int i = -1; i <= 1; ++i) {
                for (int j = -1; j <= 1; ++j) {
                    if (i == 0 && j == 0 || i != 0 && j != 0) {
                        continue;
                    }
                    if (curRow + i < 0 || curRow + i >= maze.getRow() || curCol + j < 0 || curCol + j >= maze.getCol()) {
                        continue;
                    }
                    if (!maze.isWall(curRow + i, curCol + j) && !isVisited[curRow + i][curCol + j]) {
                        openCells.add(new EntityPosition(curRow + i, curCol + j));
                    }
                }
            }
        }
        for (int i = 0; i < maze.getRow(); ++i) {
            for (int j = 0; j < maze.getCol(); ++j) {
                if (!maze.isWall(i, j) && !isVisited[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean checkConstraint() {
        for (int i = 0; i < (maze.getRow() - 1); i++) {
            for (int j = 0; j < (maze.getCol() - 1); j++) {
                if (!fourBlocksConstraintSatisfied(i, j)) {
                    return false;
                }
            }
        }
        return hasPathToAllOpenCells();
    }

    private void updateState() {
        if (mouse.equals(cat1) || mouse.equals(cat2) || mouse.equals(cat3)) {
            state = GameState.LOSING;
            isMazeRevealed = true;
            return;
        } else if (mouse.equals(cheese)) {
            cheeseCollected++;
            state = GameState.CHEESE_COLLECTING;
        }
        if (cheeseCollected >= cheeseToWin) {
            state = GameState.WINNING;
            isMazeRevealed = true;
        }
    }

    private String incompleteMazeToString() {
        //TODO: Add code to convert maze into string
        StringBuilder sb = new StringBuilder();
        sb.append("\nMaze: \n");
        for (int i = 0; i < maze.getRow(); i++) {
            for (int j = 0; j < maze.getCol(); j++) {
                if (i == mouse.getYCoordinate() && j == mouse.getXCoordinate() &&
                        (mouse.equals(cat1) || mouse.equals(cat2) || mouse.equals(cat3))) {
                    sb.append(DEFAULT_MOUSE_EATEN_BY_CAT_SYMBOL);
                } else if ((i == cat1.getYCoordinate() && j == cat1.getXCoordinate())
                        || (i == cat2.getYCoordinate() && j == cat2.getXCoordinate())
                        || (i == cat3.getYCoordinate() && j == cat3.getXCoordinate())) {
                    sb.append(catSymbol);
                } else if (i == mouse.getYCoordinate() && j == mouse.getXCoordinate()) {
                    sb.append(mouseSymbol);
                } else if (i == cheese.getYCoordinate() && j == cheese.getXCoordinate()) {
                    sb.append(cheeseSymbol);
                } else if (explored[i][j]) {
                    if (maze.isWall(i, j)) {
                        sb.append(DEFAULT_WALL_SYMBOL);
                    } else {
                        sb.append(DEFAULT_OPEN_SYMBOL);
                    }
                } else {
                    sb.append(DEFAULT_UNEXPLORED_SYMBOL);
                }
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    private String completeMazeToString() {
        StringBuilder sb = new StringBuilder();
        sb.append("\nMaze: \n");
        for (int i = 0; i < maze.getRow(); i++) {
            for (int j = 0; j < maze.getCol(); j++) {
                if (i == mouse.getYCoordinate() && j == mouse.getXCoordinate() &&
                        (mouse.equals(cat1) || mouse.equals(cat2) || mouse.equals(cat3))) {
                    sb.append(DEFAULT_MOUSE_EATEN_BY_CAT_SYMBOL);
                } else if ((i == cat1.getYCoordinate() && j == cat1.getXCoordinate())
                        || (i == cat2.getYCoordinate() && j == cat2.getXCoordinate())
                        || (i == cat3.getYCoordinate() && j == cat3.getXCoordinate())) {
                    sb.append(catSymbol);
                } else if (i == mouse.getYCoordinate() && j == mouse.getXCoordinate()) {
                    sb.append(mouseSymbol);
                } else if (i == cheese.getYCoordinate() && j == cheese.getXCoordinate()) {
                    sb.append(cheeseSymbol);
                } else {
                    if (maze.isWall(i, j)) {
                        sb.append(DEFAULT_WALL_SYMBOL);
                    } else {
                        sb.append(DEFAULT_OPEN_SYMBOL);
                    }
                }
            }
            sb.append("\n");
        }

        return sb.toString();
    }


    private void exploreMaze() {
        //TODO: Add code to explore maze based on current mouse position
        int curRow = mouse.getYCoordinate();
        int curCol = mouse.getXCoordinate();

        for (int i = -1; i <= 1; ++i) {
            for (int j = -1; j <= 1; ++j) {
                int tempRow = curRow + i;
                int tempCol = curCol + j;
                if (tempRow >= 0 && tempRow < maze.getRow() && tempCol >= 0 && tempCol < maze.getCol()) {
                    explored[tempRow][tempCol] = true;
                }
            }
        }
    }

    private boolean validatePos(EntityPosition newPos) {
        int newRow = newPos.getYCoordinate();
        int newCol = newPos.getXCoordinate();
        return !maze.isWall(newRow, newCol);
    }

    public boolean isValidMove(MoveDirection direction) {
        try {
            EntityPosition newPos = nextPosition(mouse, direction);
            return (validatePos(newPos));
        } catch (Exception e) {

        }
        return false;

    }

    private EntityPosition nextPosition(EntityPosition currentPos, MoveDirection direction) {
        EntityPosition next = new EntityPosition(currentPos.getYCoordinate(), currentPos.getXCoordinate());
        if (direction == MoveDirection.UP) {
            next.setYCoordinate(next.getYCoordinate() - 1);
        } else if (direction == MoveDirection.DOWN) {
            next.setYCoordinate(next.getYCoordinate() + 1);
        } else if (direction == MoveDirection.LEFT) {
            next.setXCoordinate(next.getXCoordinate() - 1);
        } else if (direction == MoveDirection.RIGHT) {
            next.setXCoordinate(next.getXCoordinate() + 1);
        } else {
            throw new IllegalArgumentException("Invalid move direction");
        }
        return next;
    }

    private boolean moveEntity(EntityPosition entity, MoveDirection move) {
        EntityPosition newPos;
        try {
            newPos = nextPosition(entity, move);
            if (validatePos(newPos)) {
                entity.updatePos(newPos);
                return true;
            }

        } catch (Exception e) {
        }
        return false;
    }

    public void moveCats() {
        //TODO: Add code to move cats randomly
        MoveDirection[] possibleMoves = {MoveDirection.UP, MoveDirection.DOWN, MoveDirection.RIGHT, MoveDirection.LEFT};
        Random rand = new Random();

        while (!moveEntity(cat1, possibleMoves[rand.nextInt(4)])) {
        }
        while (!moveEntity(cat2, possibleMoves[rand.nextInt(4)])) {
        }
        while (!moveEntity(cat3, possibleMoves[rand.nextInt(4)])) {
        }
    }


    private void generateCheese() {
        Random rand = new Random();
        int cheeseRow;
        int cheeseCol;
        while (true) {
            cheeseRow = rand.nextInt(15);
            cheeseCol = rand.nextInt(20);
            if (!maze.isWall(cheeseRow, cheeseCol)
                    && (cheeseRow != mouse.getYCoordinate() || cheeseCol != mouse.getXCoordinate()))
                break;
        }
        cheese = new EntityPosition(cheeseRow, cheeseCol);
    }
}
