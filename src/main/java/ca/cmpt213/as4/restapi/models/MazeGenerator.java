package ca.cmpt213.as4.restapi.models;
/*
 * MazeGenerator represents a generic interface for all generator class which could be use to generate maze.
 */
public interface MazeGenerator<T>{
        public T[][] generateMaze(int row, int col);
}
