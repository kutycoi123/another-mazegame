package ca.cmpt213.as4.restapi.models;

/*
 * Maze represents a maze which can be constructed with generic type.
 * The Maze should generated using a generator algorithm which must be passed into constructor
 */
public class Maze<E extends MazeEntity, T extends MazeGenerator<E>> {
    private int row, col;
    private E[][] maze; //Generic maze type

    public Maze(int row, int col, T generator) {
        this.row = row;
        this.col = col;
        maze = generator.generateMaze(row, col);
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public boolean isWall(int row, int col) {
        if (row < 0 || row > this.row || col < 0 || col > this.col) {
            throw new IndexOutOfBoundsException();
        }
        //return maze[row][col] == MazeEntity.WALL;
        return maze[row][col].isWall();
    }

    public boolean[][] getBooleanMaze() {
        boolean[][] booleanMaze = new boolean[row][col];
        for (int i = 0; i < row; ++i) {
            for (int j = 0; j < col; ++j) {
                booleanMaze[i][j] = maze[i][j].isWall();
            }
        }
        return booleanMaze;
    }
}
