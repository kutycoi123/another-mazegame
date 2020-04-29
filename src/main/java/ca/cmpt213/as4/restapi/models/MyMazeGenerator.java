package ca.cmpt213.as4.restapi.models;

import java.util.ArrayList;
/*
 * MyMazeGenerator is responsible for generating maze based on Prim algorithm.
 * We used the idea of Prim algorithm as well as applying some techniques to generate a qualified maze for this project
 * Reference: http://jonathanzong.com/blog/2012/11/06/maze-generation-with-prims-algorithm
 */
public class MyMazeGenerator implements MazeGenerator<MyMazeEntity> {
    class Position {
        int row, col;
        Position parent;

        public Position(int row, int col, Position parent){
            this.row = row;
            this.col = col;
            this.parent = parent;
        }

        public Position getOppositeEntity(){
            if(this.row == this.parent.row && this.col == this.parent.col)
                return this;
            return new Position(this.row+(this.row - this.parent.row), this.col + (this.col-this.parent.col), this);
        }
    }
    private int randInRange(int min, int max) {

        if (min >= max) {
            throw new IllegalArgumentException("max must be greater than min");
        }

        return (int)(Math.random() * ((max - min) + 1)) + min;
    }
    public MyMazeEntity[][] generateMaze(int row, int col){
        MyMazeEntity[][] maze = new MyMazeEntity[row][col];

        for(int i = 0; i < row; ++i){
            for(int j = 0; j < col; ++j){
                maze[i][j] = MyMazeEntity.WALL;
            }
        }

        int maxRow = row - 1, minRow = 0;
        int maxCol = col - 1, minCol = 0;
        Position startPos =  new Position(randInRange(minRow+1, maxRow-1), randInRange(minCol+1, maxCol-1), null);
        maze[startPos.row][startPos.col] = MyMazeEntity.OPEN;

        ArrayList<Position> walls = new ArrayList<>();

        if (startPos.col - 1 >= 0) {
            walls.add(new Position(startPos.row, startPos.col - 1, startPos));
        }
        if (startPos.col + 1 < col) {
            walls.add(new Position(startPos.row, startPos.col+1, startPos));
        }
        if (startPos.row - 1 >= 0) {
            walls.add(new Position(startPos.row - 1, startPos.col, startPos));
        }
        if (startPos.row + 1 < row) {
            walls.add(new Position(startPos.row + 1, startPos.col, startPos));
        }

        while(!walls.isEmpty()){
            Position current = walls.remove((int)(Math.random() * walls.size()));

            Position oppositeOfCurrent = current.getOppositeEntity();
            //Out of bound
            if (oppositeOfCurrent.row < 0 || oppositeOfCurrent.row > row-1 || oppositeOfCurrent.col < 0 || oppositeOfCurrent.col > col-1) {
                continue;
            }

            if(maze[current.row][current.col] == MyMazeEntity.WALL &&
                    maze[oppositeOfCurrent.row][oppositeOfCurrent.col] == MyMazeEntity.WALL) { //If both are walls
                //Remove walls to put cats and mouse in the 4 corners of the maze
                if ((current.row == 1 && current.col == 1) || (current.row == 1 && current.col == col - 2)||
                        (current.row == row - 2 && current.col == 1) || (current.row == row - 2 && current.col == col - 2)) {
                    maze[current.row][current.col] = MyMazeEntity.OPEN;
                    continue;
                }
                if (current.row != 0 && current.row != row - 1 && current.col != 0 && current.col != col - 1) {
                    maze[current.row][current.col] = MyMazeEntity.OPEN;
                }
                if (oppositeOfCurrent.row != 0 && oppositeOfCurrent.row != row - 1 &&
                        oppositeOfCurrent.col != 0 && oppositeOfCurrent.col != col -1) {
                    maze[oppositeOfCurrent.row][oppositeOfCurrent.col] = MyMazeEntity.OPEN;
                } else {
                    continue;
                }

                for (int i = -1; i <= 1; ++i) {
                    for (int j = -1; j <= 1; ++j) {
                        if (i == 0 && j == 0 || i != 0 && j != 0) {
                            continue;
                        }
                        if (oppositeOfCurrent.row + i >= 0 && oppositeOfCurrent.row + i < row &&
                                oppositeOfCurrent.col + j >= 0 && oppositeOfCurrent.col + j < col) {
                            if (maze[oppositeOfCurrent.row+i][oppositeOfCurrent.col + j] == MyMazeEntity.WALL) {
                                walls.add(new Position(oppositeOfCurrent.row+i, oppositeOfCurrent.col+j, oppositeOfCurrent));
                            }
                        }
                    }
                }
            }
        }

        return maze;
    }
}
